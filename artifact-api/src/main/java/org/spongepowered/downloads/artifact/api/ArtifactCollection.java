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
package org.spongepowered.downloads.artifact.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.jackson.datatype.VavrModule;

import java.util.StringJoiner;

@JsonSerialize
public final class ArtifactCollection {

    @Schema(required = false, description = "A map of artifact kind to artifact information")
    @JsonProperty(value = "components")
    private final Map<String, Artifact> artifactComponents;

    @Schema(
        required = false,
        description = "The group for an artifact collection"
    )
    @JsonProperty
    private final Group group;

    @Schema(required = true)
    @JsonProperty(required = true)
    private final String artifactId;

    @Schema(required = true)
    @JsonProperty(required = true)
    private final String version;

    @JsonIgnore
    private final String mavenCoordinates;

    @Schema(required = true)
    @JsonProperty
    private final String mavenVersion;

    public ArtifactCollection(
        final Group group,
        final String artifactId,
        final String version
    ) {
        this.artifactComponents = HashMap.empty();
        this.group = group;
        this.artifactId = artifactId;
        this.version = version;
        this.mavenVersion = version;
        this.mavenCoordinates = new StringJoiner(":")
            .add(this.group.getGroupCoordinates())
            .add(this.artifactId)
            .add(this.version)
            .toString();
    }

    public ArtifactCollection(
        final Map<String, Artifact> components,
        final Group group,
        final String artifactId,
        final String version
    ) {
        this.artifactComponents = components;
        this.group = group;
        this.artifactId = artifactId;
        this.version = version;
        this.mavenVersion = version;
        this.mavenCoordinates = new StringJoiner(":")
            .add(this.group.getGroupCoordinates())
            .add(this.artifactId)
            .add(this.version)
            .toString();
    }

    @JsonCreator
    public ArtifactCollection(
        final Map<String, Artifact> artifactComponents,
        final Group group,
        final String artifactId,
        final String version,
        final String mavenVersion
    ) {
        this.artifactComponents = artifactComponents;
        this.group = group;
        this.artifactId = artifactId;
        this.version = version;
        this.mavenVersion = mavenVersion;
        this.mavenCoordinates = new StringJoiner(":")
            .add(this.group.getGroupCoordinates())
            .add(this.artifactId)
            .add(this.mavenVersion)
            .toString();
    }

    public Map<String, Artifact> getArtifactComponents() {
        return this.artifactComponents;
    }

    public Group getGroup() {
        return this.group;
    }

    public String getArtifactId() {
        return this.artifactId;
    }

    public String getVersion() {
        return this.version;
    }

    public String getMavenVersion() {
        return this.mavenVersion;
    }

    public String getMavenCoordinates() {
        return this.mavenCoordinates;
    }
}
