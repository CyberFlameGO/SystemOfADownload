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
package org.spongepowered.downloads.guice;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.spongepowered.downloads.buisness.MetadataDownload;
import org.spongepowered.downloads.buisness.UploadProcessor;
import org.spongepowered.downloads.buisness.impl.MetadataDownloadImpl;
import org.spongepowered.downloads.buisness.impl.UploadProcessorImpl;
import org.spongepowered.downloads.config.AppConfig;
import org.spongepowered.downloads.database.DatabaseConnectionPool;
import org.spongepowered.downloads.database.DatabasePersistence;
import org.spongepowered.downloads.database.impl.HikariDatabaseConnectionPool;
import org.spongepowered.downloads.database.impl.PostgresDatabasePersistence;

/**
 * Contains all the Guice bindings
 */
public class InjectorModule extends AbstractModule {

    private final AppConfig appConfig;
    private final Gson gson;

    /**
     * Create the module.
     *
     * @param gson The {@link Gson} object to use around the application.
     * @param appConfig The {@link AppConfig} that configures this application.
     */
    public InjectorModule(Gson gson, AppConfig appConfig) {
        this.gson = gson;
        this.appConfig = appConfig;
    }

    @Override
    protected void configure() {
        bind(Gson.class).toInstance(this.gson);
        bind(AppConfig.class).toInstance(this.appConfig);
        bind(DatabaseConnectionPool.class).toInstance(new HikariDatabaseConnectionPool(this.appConfig.getDatabaseConfig()));
        bind(DatabasePersistence.class).to(PostgresDatabasePersistence.class).in(Singleton.class);
        bind(MetadataDownload.class).to(MetadataDownloadImpl.class).in(Singleton.class);
        bind(UploadProcessor.class).to(UploadProcessorImpl.class).in(Singleton.class);
    }

}
