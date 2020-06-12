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

import com.google.common.collect.ImmutableMap;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/**
 * The application configuration object.
 */
public class AppConfig {

    @JsonAdapter(PathTypeAdapter.class)
    private Path tempDirectory = Path.of("tmp");
    private Database database;
    private Map<String, Product> products;

    /**
     * Gets the temporary directory this app should use for any temporary
     * processing that is required.
     *
     * @return The temporary directory.
     */
    public Path getTempDirectory() {
        return this.tempDirectory;
    }

    /**
     * Gets the Database Configuration.
     *
     * @return The {@link Database}
     */
    public Database getDatabaseConfig() {
        return this.database;
    }

    /**
     * Gets the {@link Product} for the given name.
     *
     * @param name The name of the product
     * @return The product
     * @throws IllegalArgumentException if the product supplied is not supported
     *      by this indexer.
     */
    public Product getProduct(String name) throws IllegalArgumentException {
        Product product = this.products.get(name);
        if (product == null) {
            throw new IllegalArgumentException("Product was provided that is not supported by this indexer.");
        }
        return product;
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

    /**
     * Represents a product that is accepted by this indexer.
     */
    public static class Product {

        @SerializedName("major-version-override")
        private Map<String, Product> majorVersionOverride = ImmutableMap.of();
        private Map<String, String> v1rest;
        private String name;
        private String repo;
        private String groupid;
        private String artifactid;

        /**
         * Used for SpongeVanilla, if a version will have a different repo
         * to the norm (API 7 vs 8), use the override specified here instead.
         *
         * @return The overrides, if any
         */
        public Map<String, Product> getMajorVersionOverride() {
            return majorVersionOverride;
        }

        /**
         * The name of the product.
         *
         * @return The name
         */
        public String getName() {
            return this.name;
        }

        /**
         * The Git repo for the product.
         *
         * @return The repo
         */
        public String getRepo() {
            return this.repo;
        }

        /**
         * If this is eligible for display in the v1 REST API, this will be
         * non-null. Such elements will be added to the response as appropriate.
         *
         * @return The v1 REST tags, if necessary.
         */
        @Nullable
        public Map<String, String> getV1rest() {
            return this.v1rest;
        }

        /**
         * Gets the artifact ID for the product.
         *
         * @return The artifact ID
         */
        public String getArtifactid() {
            return this.artifactid;
        }

        /**
         * Gets the group ID for the product.
         *
         * @return The group ID
         */
        public String getGroupid() {
            return this.groupid;
        }
    }

    private static class PathTypeAdapter extends TypeAdapter<Path> {

        @Override
        public void write(JsonWriter out, Path value) throws IOException {
            // we're not using this
        }

        @Override
        public Path read(JsonReader in) throws IOException {
            return Path.of(in.nextString());
        }
    }

}
