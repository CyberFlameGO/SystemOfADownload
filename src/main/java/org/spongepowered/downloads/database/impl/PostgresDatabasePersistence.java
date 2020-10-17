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
package org.spongepowered.downloads.database.impl;

import com.google.inject.Inject;
import org.spongepowered.downloads.config.AppConfig;
import org.spongepowered.downloads.database.DatabaseConnectionPool;
import org.spongepowered.downloads.database.DatabasePersistence;
import org.spongepowered.downloads.pojo.data.Downloadable;
import org.spongepowered.downloads.pojo.query.DownloadableQuery;

import java.util.List;

/**
 * Postgres database persistence.
 */
public class PostgresDatabasePersistence implements DatabasePersistence {

    private final DatabaseConnectionPool connectionPool;

    /**
     * Constructs this object.
     *
     * @param connectionPool The {@link DatabaseConnectionPool}
     */
    @Inject
    public PostgresDatabasePersistence(final DatabaseConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Downloadable> getDownloadable(final DownloadableQuery query) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createProduct(final AppConfig.Product product) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createDownloadable(final Downloadable downloadable) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void markBroken(final DownloadableQuery query) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateChangelog(final DownloadableQuery query, final String changelog) {

    }
}
