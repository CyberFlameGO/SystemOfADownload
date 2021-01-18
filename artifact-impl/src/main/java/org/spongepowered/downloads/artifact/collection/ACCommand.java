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
package org.spongepowered.downloads.artifact.collection;

import akka.NotUsed;
import akka.actor.typed.ActorRef;
import com.lightbend.lagom.serialization.Jsonable;
import org.spongepowered.downloads.artifact.api.ArtifactCollection;
import org.spongepowered.downloads.artifact.api.Group;
import org.spongepowered.downloads.artifact.api.query.GetVersionsResponse;

import java.io.Serial;
import java.util.Objects;

public interface ACCommand extends Jsonable {

    final class RegisterCollection implements ACCommand {
        @Serial private static final long serialVersionUID = 0L;
        public final Group group;
        public final ArtifactCollection collection;
        public final ActorRef<NotUsed> replyTo;

        public RegisterCollection(
            final Group group, final ArtifactCollection collection,
            final ActorRef<NotUsed> replyTo
        ) {
            this.group = group;
            this.collection = collection;
            this.replyTo = replyTo;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || obj.getClass() != this.getClass()) {
                return false;
            }
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

    final class GetVersions implements ACCommand {
        @Serial private static final long serialVersionUID = 0L;
        public final String groupId;
        public final String artifactId;
        public final ActorRef<GetVersionsResponse> replyTo;

        public GetVersions(
            final String groupId, final String artifactId,
            final ActorRef<GetVersionsResponse> replyTo
        ) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.replyTo = replyTo;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || obj.getClass() != this.getClass()) {
                return false;
            }
            final var that = (GetVersions) obj;
            return Objects.equals(this.groupId, that.groupId) &&
                Objects.equals(this.artifactId, that.artifactId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.groupId, this.artifactId);
        }

        @Override
        public String toString() {
            return "GetVersions[" +
                "groupId=" + this.groupId + ", " +
                "artifactId=" + this.artifactId + ']';
        }
    }
}
