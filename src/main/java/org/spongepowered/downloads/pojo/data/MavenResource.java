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
package org.spongepowered.downloads.pojo.data;

import java.util.Map;
import java.util.Set;

public final class MavenResource {

    private final boolean isRelease;
    private final String version;
    private final Set<Download> downloadSet;

    public MavenResource(final boolean isRelease, final String version, final Set<Download> downloadSet) {
        this.isRelease = isRelease;
        this.version = version;
        this.downloadSet = downloadSet;
    }

    public boolean isRelease() {
        return this.isRelease;
    }

    public String getVersion() {
        return this.version;
    }

    public Set<Download> getDownloadSet() {
        return this.downloadSet;
    }

    public static final class Download {

        private final String identifier;
        private final String downloadUrl;
        private final Map<String, String> checksums;

        public Download(final String identifier, final String downloadUrl, final Map<String, String> checksums) {
            this.identifier = identifier;
            this.downloadUrl = downloadUrl;
            this.checksums = checksums;
        }

        public String getIdentifier() {
            return this.identifier;
        }

        public String getDownloadUrl() {
            return this.downloadUrl;
        }

        public Map<String, String> getChecksums() {
            return this.checksums;
        }
    }

}
