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
package org.spongepowered.synchronizer.resync;

import org.spongepowered.downloads.artifact.api.ArtifactCoordinates;
import org.spongepowered.downloads.maven.artifact.ArtifactMavenMetadata;
import org.spongepowered.downloads.maven.artifact.Versioning;

final class SyncState {

    public final String groupId;
    public final String artifactId;
    public final String lastUpdated;
    public final ArtifactMavenMetadata versions;

    static final SyncState EMPTY = new SyncState("", new ArtifactMavenMetadata("", "", new Versioning()));

    public SyncState(
        final String lastUpdated,
        final ArtifactMavenMetadata versions
    ) {
        this.lastUpdated = lastUpdated;
        this.versions = versions;
        this.groupId = versions.groupId();
        this.artifactId = versions.artifactId();
    }

    ArtifactCoordinates coordinates() {
        return new ArtifactCoordinates(this.groupId, this.artifactId);
    }
}
