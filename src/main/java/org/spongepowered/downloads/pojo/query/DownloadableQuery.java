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
package org.spongepowered.downloads.pojo.query;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.downloads.pojo.data.Downloadable;

/**
 * Specifies which {@link Downloadable}s to obtain from the database.
 */
public class DownloadableQuery {

    private final String projectId;
    @Nullable private final String version;
    private final boolean recommendedOnly;

    /**
     * Constructs this query object.
     *
     * @param projectId The project ID.
     * @param version The version, if a specific version is required
     * @param recommendedOnly If only recommended versions are to be returned
     */
    public DownloadableQuery(String projectId, @Nullable String version, boolean recommendedOnly) {
        this.projectId = projectId;
        this.version = version;
        this.recommendedOnly = recommendedOnly;
    }

}
