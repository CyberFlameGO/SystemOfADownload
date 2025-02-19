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
package org.spongepowered.downloads.versions.collection;

import com.lightbend.lagom.javadsl.persistence.AggregateEvent;
import com.lightbend.lagom.javadsl.persistence.AggregateEventShards;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTag;
import com.lightbend.lagom.javadsl.persistence.AggregateEventTagger;
import com.lightbend.lagom.serialization.Jsonable;
import org.spongepowered.downloads.artifact.api.MavenCoordinates;

import java.util.Objects;
import java.util.StringJoiner;

public interface VersionedArtifactEvent extends AggregateEvent<VersionedArtifactEvent>, Jsonable {

    AggregateEventShards<VersionedArtifactEvent> TAG = AggregateEventTag.sharded(VersionedArtifactEvent.class, 10);

    @Override
    default AggregateEventTagger<VersionedArtifactEvent> aggregateTag() {
        return TAG;
    }

    String asMavenCoordinates();

    class VersionRegistered implements VersionedArtifactEvent {

        public final MavenCoordinates coordinates;

        public VersionRegistered(final MavenCoordinates coordinates) {
            this.coordinates = coordinates;
        }



        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            VersionRegistered that = (VersionRegistered) o;
            return Objects.equals(coordinates, that.coordinates);
        }

        @Override
        public int hashCode() {
            return Objects.hash(coordinates);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", VersionRegistered.class.getSimpleName() + "[", "]")
                .add("coordinates=" + coordinates)
                .toString();
        }

        @Override
        public String asMavenCoordinates() {
            return this.coordinates.asStandardCoordinates();
        }
    }
}
