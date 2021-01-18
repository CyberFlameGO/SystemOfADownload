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
package org.spongepowered.downloads.artifact.api.query;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lightbend.lagom.serialization.Jsonable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.vavr.collection.List;
import org.spongepowered.downloads.artifact.api.ArtifactCollection;

import java.io.Serial;
import java.util.Objects;
import java.util.StringJoiner;

public final class ArtifactRegistration {

    @JsonSerialize
    public static final class RegisterArtifact {

        @JsonProperty(required = true)
        private final String artifactId;
        @JsonProperty(required = true)
        private final String displayName;
        @JsonProperty(required = false)
        private final String version;

        @JsonCreator
        public RegisterArtifact(final String artifactId, final String displayName, final String version) {
            this.artifactId = artifactId;
            this.displayName = displayName;
            this.version = version;
        }

        public String getArtifactId() {
            return this.artifactId;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public String getVersion() {
            return this.version;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final RegisterArtifact that = (RegisterArtifact) o;
            return Objects.equals(this.artifactId, that.artifactId) && Objects.equals(
                this.displayName, that.displayName) && Objects.equals(this.version, that.version);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.artifactId, this.displayName, this.version);
        }

        @Override
        public String toString() {
            return new StringJoiner(
                ", ",
                RegisterArtifact.class.getSimpleName() + "[",
                "]"
            )
                .add("artifactId='" + this.artifactId + "'")
                .add("displayName='" + this.displayName + "'")
                .add("version='" + this.version + "'")
                .toString();
        }
    }

    @JsonSerialize
    public static final class RegisterCollection {

        @Schema(required = true) @JsonProperty private final ArtifactCollection collection;

        @JsonCreator
        public RegisterCollection(@Schema(required = true) final ArtifactCollection collection) {
            this.collection = collection;
        }

        @Schema(required = true)
        public ArtifactCollection collection() {
            return this.collection;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            final var that = (RegisterCollection) obj;
            return Objects.equals(this.collection, that.collection);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.collection);
        }

        @Override
        public String toString() {
            return "RegisterCollection[" +
                "collection=" + this.collection + ']';
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
    @JsonSubTypes({
        @JsonSubTypes.Type(value = Response.GroupMissing.class, name = "UnknownGroup"),
        @JsonSubTypes.Type(value = Response.ArtifactAlreadyRegistered.class, name = "AlreadyRegistered"),
        @JsonSubTypes.Type(value = Response.RegisteredArtifact.class, name = "RegistrationSuccess"),
    })
    public interface Response extends Jsonable {

        @JsonSerialize
        final class ArtifactAlreadyRegistered implements Response {

            @Serial private static final long serialVersionUID = -3135793273231868113L;

            @JsonProperty
            private final String artifactName;
            @JsonProperty
            private final String groupId;

            public ArtifactAlreadyRegistered(final String artifactName, final String groupId) {
                this.artifactName = artifactName;
                this.groupId = groupId;
            }

            public String artifactName() {
                return this.artifactName;
            }

            public String groupId() {
                return this.groupId;
            }

            @Override
            public boolean equals(final Object obj) {
                if (obj == this) return true;
                if (obj == null || obj.getClass() != this.getClass()) return false;
                final var that = (ArtifactAlreadyRegistered) obj;
                return Objects.equals(this.artifactName, that.artifactName) &&
                    Objects.equals(this.groupId, that.groupId);
            }

            @Override
            public int hashCode() {
                return Objects.hash(this.artifactName, this.groupId);
            }

            @Override
            public String toString() {
                return "ArtifactAlreadyRegistered[" +
                    "artifactName=" + this.artifactName + ", " +
                    "groupId=" + this.groupId + ']';
            }
        }

        @JsonSerialize
        final class RegisteredArtifact implements Response {

            @Serial private static final long serialVersionUID = 9042959235854086148L;

            @Schema(required = true)
            @JsonProperty(required = true)
            private final ArtifactCollection artifact;

            public RegisteredArtifact(final ArtifactCollection artifact) {
                this.artifact = artifact;
            }

            public String getMavenCoordinates() {
                return this.artifact.getMavenCoordinates();
            }

            public ArtifactCollection artifact() {
                return this.artifact;
            }

            @Override
            public boolean equals(final Object obj) {
                if (obj == this) return true;
                if (obj == null || obj.getClass() != this.getClass()) return false;
                final var that = (RegisteredArtifact) obj;
                return Objects.equals(this.artifact, that.artifact);
            }

            @Override
            public int hashCode() {
                return Objects.hash(this.artifact);
            }

            @Override
            public String toString() {
                return "RegisteredArtifact[" +
                    "artifact=" + this.artifact + ']';
            }

        }

        @JsonSerialize
        final class GroupMissing implements Response {
            @Serial private static final long serialVersionUID = 8763121568817311891L;

            @JsonProperty("groupId")
            private final String s;

            public GroupMissing(final String s) {
                this.s = s;
            }

            public String s() {
                return this.s;
            }

            @Override
            public boolean equals(final Object obj) {
                if (obj == this) return true;
                if (obj == null || obj.getClass() != this.getClass()) return false;
                final var that = (GroupMissing) obj;
                return Objects.equals(this.s, that.s);
            }

            @Override
            public int hashCode() {
                return Objects.hash(this.s);
            }

            @Override
            public String toString() {
                return "GroupMissing[" +
                    "s=" + this.s + ']';
            }
        }

    }
}
