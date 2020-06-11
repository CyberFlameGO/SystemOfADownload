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

import java.net.URL;
import java.time.ZonedDateTime;

/**
 * Represents a download
 */
public class Downloadable {

    private final URL location;
    private final String version;
    private final String author;
    private final String changelog;
    private final ZonedDateTime zonedDateTime;

    /**
     * Constructs this data object
     *
     * @param location The URL of the object
     * @param version The version of the object
     * @param author The author of the object
     * @param changelog The changelog associated with the object
     * @param zonedDateTime The time the object was created
     */
    public Downloadable(URL location,
            String version,
            String author,
            String changelog,
            ZonedDateTime zonedDateTime) {
        this.location = location;
        this.version = version;
        this.author = author;
        this.changelog = changelog;
        this.zonedDateTime = zonedDateTime;
    }

    /**
     * The URL of the object.
     *
     * @return The URL
     */
    public URL getLocation() {
        return this.location;
    }

    /**
     * The version of the object.
     *
     * @return The version
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * The author of the object.
     *
     * @return The author
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * The changelog.
     *
     * @return The changelog
     */
    public String getChangelog() {
        return this.changelog;
    }

    /**
     * The date the object was made.
     *
     * @return The date
     */
    public ZonedDateTime getZonedDateTime() {
        return this.zonedDateTime;
    }

}
