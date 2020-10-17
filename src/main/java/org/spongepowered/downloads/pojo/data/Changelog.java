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

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * Represents a changelog.
 */
public class Changelog {

    private final List<Entry> entries;
    private final Map<String, Changelog> submodules;

    /**
     * Constructs this changelog.
     *
     * @param entries The changelog entries
     * @param submodules Any submodules
     */
    public Changelog(final List<Entry> entries, final Map<String, Changelog> submodules) {
        this.entries = entries;
        this.submodules = submodules;
    }

    /**
     * Gets the changelog entries.
     *
     * @return The entries.
     */
    public List<Entry> getEntries() {
        return entries;
    }

    /**
     * Gets the changelogs for each direct submodule.
     *
     * @return The changelogs for each direct submodule
     */
    public Map<String, Changelog> getSubmodules() {
        return submodules;
    }

    /**
     * An entry in the changelog.
     */
    public static class Entry {

        private final String commit;
        private final String message;
        private final String author;
        private final ZonedDateTime dateTime;

        /**
         * Constructs this entry.
         *
         * @param commit The commit
         * @param message The message
         * @param author The author
         * @param dateTime The commit time
         */
        public Entry(final String commit, final String message, final String author, final ZonedDateTime dateTime) {
            this.commit = commit;
            this.message = message;
            this.author = author;
            this.dateTime = dateTime;
        }

        /**
         * The commit this entry is for.
         *
         * @return The commit
         */
        public String getCommit() {
            return this.commit;
        }

        /**
         * The commit message.
         *
         * @return The message
         */
        public String getMessage() {
            return this.message;
        }

        /**
         * The commit author.
         *
         * @return The author
         */
        public String getAuthor() {
            return this.author;
        }

        /**
         * The {@link ZonedDateTime} the commit was authored.
         *
         * @return The date
         */
        public ZonedDateTime getDateTime() {
            return this.dateTime;
        }
    }

}
