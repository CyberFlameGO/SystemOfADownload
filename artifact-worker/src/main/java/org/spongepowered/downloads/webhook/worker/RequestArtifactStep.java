/*
 * This file is part of SystemOfADownload, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://spongepowered.org/>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.downloads.webhook.worker;

import akka.Done;
import akka.NotUsed;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.spongepowered.downloads.artifact.api.Artifact;
import org.spongepowered.downloads.artifact.api.ArtifactCollection;
import org.spongepowered.downloads.artifact.api.MavenCoordinates;
import org.spongepowered.downloads.artifact.api.query.ArtifactRegistration;
import org.spongepowered.downloads.artifact.api.query.GroupResponse;
import org.spongepowered.downloads.webhook.ScrapedArtifactEvent;
import org.spongepowered.downloads.webhook.sonatype.Component;
import org.spongepowered.downloads.webhook.sonatype.SonatypeClient;

import java.time.Duration;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.regex.Pattern;

public final class RequestArtifactStep
    implements WorkerStep<ScrapedArtifactEvent.ArtifactRequested> {
    public static final Logger LOGGER = LogManager.getLogger("ArtifactRequestedStep");
    private static final Marker MARKER = MarkerManager.getMarker("ARTIFACT_REQUESTED");
    private static final Pattern filePattern = Pattern.compile("(dev\\b|\\d+|shaded).jar$");
    private final ObjectMapper objectMapper;

    public RequestArtifactStep(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Try<Done> processEvent(
        final SonatypeArtifactWorkerService service,
        final ScrapedArtifactEvent.ArtifactRequested event
    ) {
        return Try.of(
            () -> SonatypeArtifactWorkerService.authorizeInvoke(service.artifacts.getGroup(event.mavenCoordinates().split(":")[0]))
                .invoke()
                .thenComposeAsync(response -> {
                    if (response instanceof GroupResponse.Available) {
                        final var available = (GroupResponse.Available) response;
                        return this.processInitializationWithGroup(service, event, available);
                    }
                    return CompletableFuture.completedFuture(Done.done());
                }).toCompletableFuture()
                .join()
        );
    }

    @Override
    public Marker marker() {
        return MARKER;
    }

    @Override
    public Logger logger() {
        return LOGGER;
    }

    private CompletionStage<Done> processInitializationWithGroup(
        final SonatypeArtifactWorkerService service,
        final ScrapedArtifactEvent.ArtifactRequested event,
        final GroupResponse.Available available
    ) {
        final SonatypeClient client = SonatypeClient.configureClient(this.objectMapper).apply();
        final Try<Component> componentTry = client.resolveMavenArtifactWithComponentVersion(
            event.mavenGroupId(), event.mavenArtifactId(), event.componentVersion());
        final var newCollection = componentTry.map(component -> {
            final Component.Asset base = component.assets()
                // First, try "finding" the most appropriate jar
                .filter(asset -> filePattern.matcher(asset.path()).matches())
                .headOption()
                .getOrElse(() -> component.assets()
                    // Or else, just get the jar with the shortest path name length
                    .filter(asset -> asset.path().endsWith(".jar"))
                    .sortBy(Component.Asset::path)
                    .head()
                );
            final var artifacts = RequestArtifactStep.gatherArtifacts(available, component, base);
            final Map<String, Artifact> artifactByVariant = artifacts.toMap(
                artifact -> artifact.variant,
                Function.identity()
            );
            final String tagVersion = component.assets()
                .filter(asset -> asset.path().endsWith(".pom"))
                .headOption()
                .map(client::resolvePomVersion)
                .flatMap(Try::get)
                .getOrElse(component::version);
            final var mavenCoordinates = MavenCoordinates.parse(
                available.group().groupCoordinates + ":" + event.mavenArtifactId() + ":" + event.componentVersion());
            final var updatedCollection = new ArtifactCollection(
                artifactByVariant,
                mavenCoordinates
            );
            return SonatypeArtifactWorkerService.authorizeInvoke(service.artifacts.registerArtifacts(available.group().getGroupCoordinates()))
                .invoke(new ArtifactRegistration.RegisterArtifact(component.id(), component.name(), component.version()))
                .thenCompose(done -> service
                    .getProcessingEntity(event.mavenCoordinates())
                    .<NotUsed>ask(replyTo -> new ScrapedArtifactCommand.AssociateMetadataWithCollection(updatedCollection, component,
                        tagVersion,
                        replyTo
                    ), Duration.ofSeconds(30))
                    .thenApply(notUsed -> Done.done())
                ).thenCompose(response -> SonatypeArtifactWorkerService.authorizeInvoke(service.artifacts
                    .registerTaggedVersion(event.mavenCoordinates(), tagVersion))
                    .invoke()
                    .thenApply(notUsed -> Done.done())
                );
        });
        return newCollection.getOrElseGet(
            throwable -> CompletableFuture.completedFuture(Done.done())
        );
    }


    private static List<Artifact> gatherArtifacts(
        final GroupResponse.Available available, final Component component, final Component.Asset base
    ) {
        final var baseName = getBaseName(base.path());
        final var rawCoordinates = new StringJoiner(":")
            .add(available.group.groupCoordinates)
            .add(component.id())
            .add(component.version())
            .toString();
        final var coordinates = MavenCoordinates.parse(rawCoordinates);
        final var variants = component.assets().filter(asset -> asset.path().endsWith(".jar"))
            .filter(jar -> !jar.equals(base))
            .map(jar -> {
                final var variant = jar.path().replace(baseName, "").replace(".jar", "");
                return new Artifact(
                    variant, coordinates, jar.downloadUrl(),
                    jar.checksum().md5(), jar.checksum().sha1()
                );
            });
        return variants.prepend(
            new Artifact("base", coordinates, base.downloadUrl(),
                base.checksum().md5(), base.checksum().sha1()
            ));
    }

    /*
This assumes that the jars accepted as the "first" are either ending with a number (like date time)
or "dev" or "shaded" or "universal" jars. This also assumes that the jar being asked to get the
base name for will be the jar that has the shortest file name length.
 */
    private static String getBaseName(final String path) {
        return path.replace(".jar", "")
            .replace("dev", "")
            .replace("shaded", "")
            .replace("universal", "")
            ;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this || obj != null && obj.getClass() == this.getClass();
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return "RequestArtifactStep[]";
    }

}
