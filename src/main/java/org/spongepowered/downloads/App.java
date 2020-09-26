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
package org.spongepowered.downloads;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.inject.Guice;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.downloads.config.AppConfig;
import org.spongepowered.downloads.exception.StatusCodeException;
import org.spongepowered.downloads.guice.InjectorModule;
import spark.Spark;

import java.io.IOException;
import java.nio.file.Path;

/**
 * The entry point to the application.
 */
public class App {

    private static final String CONFIG_LOCATION = "appconfig.json";
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    /**
     * Entrypoint for the app.
     *
     * @param args entrypoint args
     */
    public static void main(final String[] args) {
        try {
            final var gson = new Gson();

            // This creation contains classes that will be instantiated as
            // "Eager Singletons". These will initialise the application for us
            // so we need to do no more here, and we won't need the injector
            // once done.
            Guice.createInjector(new InjectorModule(gson, load(), App.LOGGER));

            // If a StatusCodeException is raised, we just halt with the given status code.
            // We may want to improve this at some point, for now, this will do.
            Spark.exception(
                    StatusCodeException.class,
                    ((exception, request, response) ->
                            Spark.halt(exception.statusCode(), exception.getMessage())));
        } catch (final Throwable e) {
            App.LOGGER.error("Unable to start server: {}", e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    /**
     * Loads the file and populates a new {@link AppConfig}.
     *
     * @return The {@link AppConfig}
     * @throws IOException if the file could not be loaded
     * @throws ObjectMappingException if the file could not be mapped
     */
    public static AppConfig load() throws IOException, ObjectMappingException {
        final var configPath = Path.of(App.CONFIG_LOCATION);
        final var configLoader = GsonConfigurationLoader.builder()
                .setPath(configPath)
                .build();
        final var node = configLoader.load();

        // create the defaults and merge them in
        final var configNode = configLoader.createEmptyNode();
        configNode.setValue(TypeToken.of(AppConfig.class), new AppConfig());
        node.mergeValuesFrom(configNode);

        configLoader.save(node);
        return configNode.getValue(TypeToken.of(AppConfig.class));
    }

}
