/*
 * This file is part of SystemOfADownload, licensed under the MIT License (MIT).
 *
 * Copyright (c) "SpongePowered" <"https://www.spongepowered.org/">
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
package org.spongepowered.downloads.buisness.maven;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.downloads.config.AppConfig;
import org.spongepowered.downloads.pojo.data.MavenResource;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class MavenImpl implements Maven {

    private final AppConfig config;

    private final static String SEARCH_ROUTE = "/v1/search";
    private final static String CONTINUATION_TOKEN = "continuationToken";
    private final static String DEFAULT_IDENTIFIERS = "default";
    private final static String IDENTIFIER_GROUP = "identifier";

    @Inject
    public MavenImpl(final AppConfig config) {
        this.config = config;
    }

    @Override
    public CompletableFuture<Set<MavenResource>> getAllResources(final AppConfig.Product product) {
        // https://repo-new.spongepowered.org/service/rest/v1/search?repository=maven-public&name=spongevanilla
        final var parameterMap = new HashMap<String, String>();
        parameterMap.put("sort", "version");
        parameterMap.put("repository", this.config.getMaven().getStandardRepo());
        parameterMap.put("name", product.getArtifactid());
        final var future = this.getAllResultsAsync(MavenImpl.SEARCH_ROUTE, parameterMap);
        return future.thenApply(input -> {
            // items [ version <string>, assets [ path/checksum ] ]
            final var resources = new HashSet<MavenResource>();
            for (final JsonArray itemsArray : input) {
                this.fill(itemsArray, resources);
            }

            return resources;
        });
    }

    @Override
    public MavenResource getResource(final AppConfig.Product product, final String version) {
        return null;
    }

    private void fill(final JsonArray itemsArray, final Set<MavenResource> resources) {
        final var releaseRepos = this.config.getMaven().getReleaseRepos();
        for (final var itemElement : itemsArray) {
            // version and name
            final var itemObject = itemElement.getAsJsonObject();
            final var name = itemObject.get("name").getAsString();
            final var version = itemObject.get("version").getAsString();

            final var filenamePatterns = Pattern.compile("^.+/" + name + "-" + version +
                    "(-(?<" + MavenImpl.IDENTIFIER_GROUP + ">[a-z]+))?\\.jar$");

            // okay, for each asset, if it matches, we take it and we store the metadata we can get from it.
            final Set<MavenResource.Download> downloads = new HashSet<>();
            for (final var assetElement : itemObject.getAsJsonArray("assets")) {
                final var assetObject = assetElement.getAsJsonObject();
                final var matcher = filenamePatterns.matcher(assetObject.get("path").getAsString());
                if (matcher.matches()) {
                    // check the identifier group, if it doesn't exist, it's "default"
                    String identifier = matcher.group(MavenImpl.IDENTIFIER_GROUP);
                    if (identifier == null) {
                        identifier = MavenImpl.DEFAULT_IDENTIFIERS;
                    }

                    final Map<String, String> checksumMap =
                            assetObject.getAsJsonObject("checksum").entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                                    x -> x.getValue().getAsString()));

                    downloads.add(new MavenResource.Download(identifier, assetObject.get("downloadUrl").getAsString(), checksumMap));
                }
            }

            final var actualRepo = itemObject.get("repository").getAsString();
            resources.add(new MavenResource(releaseRepos.contains(actualRepo), version, downloads));
        }
    }

    private CompletableFuture<Collection<JsonArray>> getAllResultsAsync(final String route, final @Nullable Map<String, String> parameters) {
        return HttpClient.newHttpClient().sendAsync(this.createRequest(route, parameters), HttpResponse.BodyHandlers.ofString())
                .thenApply(x -> this.processSearchResult(x.body(), route, parameters));
    }

    private Collection<JsonArray> processSearchResult(final String x, final String route, @Nullable final Map<String, String> parameters) {
        final var results = new ArrayList<JsonArray>();
        final var element = JsonParser.parseString(x).getAsJsonObject();
        final var continuationToken = element.get(MavenImpl.CONTINUATION_TOKEN); // could be Json Null
        if (continuationToken.isJsonPrimitive()) {
            final var newParameters = new HashMap<String, String>();
            if (parameters != null) {
                newParameters.putAll(parameters);
            }
            newParameters.put(MavenImpl.CONTINUATION_TOKEN, continuationToken.getAsString());
            try {
                results.addAll(this.processSearchResult(
                        HttpClient.newHttpClient().send(this.createRequest(route, newParameters), HttpResponse.BodyHandlers.ofString()).body(),
                        route,
                        newParameters));
            } catch (final IOException | InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException("Unable to grab all resources", e);
            }
        }

        results.add(element.getAsJsonArray("items"));
        return results;
    }

    private HttpRequest createRequest(final String route, final @Nullable Map<String, String> parameters) {
        return HttpRequest.newBuilder()
                .GET()
                .header("Accept", "application/json")
                .uri(this.getUri(route, parameters))
                .build();
    }

    private URI getUri(final String route, final @Nullable Map<String, String> parameters) {
        final var builder = new StringBuilder();
        builder.append(this.config.getMaven().getUrl()).append(this.config.getMaven().getRestEndpoint())
                .append(route);
        if (parameters != null && !parameters.isEmpty()) {
            builder.append("?");
            builder.append(parameters.entrySet().stream()
                    .map(x -> {
                        try {
                            return URLEncoder.encode(x.getKey(), StandardCharsets.UTF_8.toString()) + "=" +
                                    URLEncoder.encode(x.getValue(), StandardCharsets.UTF_8.toString());
                        } catch (final UnsupportedEncodingException e) {
                            // If this happens then things are going very wrong.
                            throw new RuntimeException(e);
                        }
                    }).collect(Collectors.joining("&")));
        }
        return URI.create(builder.toString());
    }

}
