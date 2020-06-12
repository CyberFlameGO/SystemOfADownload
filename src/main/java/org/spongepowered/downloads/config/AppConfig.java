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
package org.spongepowered.downloads.config;

/**
 * The application configuration object.
 */
public class AppConfig {

    private Database database;

    /**
     * Gets the Database Configuration.
     *
     * @return The {@link Database}
     */
    public Database getDatabaseConfig() {
        return this.database;
    }

    /**
     * Contains the Database configuration.
     */
    public static class Database {

        private String jdbcUrl;
        private boolean useDummy;

        /**
         * Gets the JDBC Url.
         *
         * @return The JDBC Url
         */
        public String getJdbcUrl() {
            return this.jdbcUrl;
        }

        /**
         * Use a dummy database, for testing without a DB.
         *
         * @return Whether to use a dummy DB
         */
        public boolean isUseDummy() {
            return this.useDummy;
        }
    }

}
