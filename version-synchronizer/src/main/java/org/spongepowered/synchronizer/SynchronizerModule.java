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
package org.spongepowered.synchronizer;

import akka.actor.typed.ActorRef;
import akka.cluster.sharding.typed.javadsl.ClusterSharding;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.lightbend.lagom.javadsl.api.ServiceInfo;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import org.pac4j.core.config.Config;
import org.spongepowered.downloads.artifact.api.ArtifactService;
import org.spongepowered.downloads.auth.SOADAuth;
import org.spongepowered.downloads.auth.utils.AuthUtils;
import org.spongepowered.downloads.versions.api.VersionsService;
import play.Environment;
import play.api.libs.concurrent.AkkaGuiceSupport;

public class SynchronizerModule extends AbstractModule  implements ServiceGuiceSupport, AkkaGuiceSupport {

    private final Environment environment;
    private final com.typesafe.config.Config config;
    private final AuthUtils auth;

    public SynchronizerModule(final Environment environment, final com.typesafe.config.Config config) {
        this.environment = environment;
        this.config = config;
        this.auth = AuthUtils.configure(config);
    }

    @Override
    protected void configure() {
        this.bindClient(VersionsService.class);
        this.bindClient(ArtifactService.class);
        this.bindServiceInfo(ServiceInfo.of("Sonatype-Synchronizer"));
        this.bind(new TypeLiteral<ActorRef<SonatypeSynchronizer.Command>>() {
            })
            .toProvider(SynchronizerProvider.class)
            .asEagerSingleton();
    }

    @Provides
    @SOADAuth
    protected Config configProvider() {
        return this.auth.config();
    }

    public static class SynchronizerProvider implements Provider<ActorRef<SonatypeSynchronizer.Command>> {
        private final ArtifactService artifactService;
        private final VersionsService versionsService;
        private final ClusterSharding clusterSharding;
        private final ObjectMapper mapper;

        @Inject
        public SynchronizerProvider(
            final ArtifactService artifactService,
            final VersionsService versionsService,
            final ClusterSharding clusterSharding,
            final ObjectMapper mapper
        ) {
            this.artifactService = artifactService;
            this.versionsService = versionsService;
            this.clusterSharding = clusterSharding;
            this.mapper = mapper;
        }

        @Override
        public ActorRef<SonatypeSynchronizer.Command> get() {
            return akka.actor.typed.ActorSystem.create(
                SonatypeSynchronizer.create(
                    this.artifactService,
                    this.versionsService,
                    this.clusterSharding,
                    this.mapper
                ), "Synchronizer");
        }
    }
}
