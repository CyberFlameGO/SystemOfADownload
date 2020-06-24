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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Guice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.downloads.config.AppConfig;
import org.spongepowered.downloads.exception.StatusCodeException;
import org.spongepowered.downloads.guice.InjectorModule;
import spark.Spark;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The entry point to the application.
 */
public class App {

    private static final String CONFIG_LOCATION = "appconfig.json";
    private static final Logger logger = LoggerFactory.getLogger(App.class);

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
            Guice.createInjector(new InjectorModule(gson, load(gson), logger));

            // If a StatusCodeException is raised, we just halt with the given status code.
            // We may want to improve this at some point, for now, this will do.
            Spark.exception(
                    StatusCodeException.class,
                    ((exception, request, response) ->
                            Spark.halt(exception.statusCode(), exception.getMessage())));
        } catch (Throwable e) {
            logger.error("Unable to start server: {}", e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    /**
     * Loads the file and populates a new {@link AppConfig}.
     *
     * @param gson The {@link Gson} instance to use to load the config
     * @return The {@link AppConfig}
     * @throws IOException if the file could not be loaded
     * @throws JsonSyntaxException if the file does not contain valid Json
     */
    public static AppConfig load(Gson gson) throws IOException, JsonSyntaxException {
        final var configPath = Path.of(CONFIG_LOCATION);
        if (Files.notExists(configPath)) {
            // Copy the default config to the current directory.
            try (var inputStream = AppConfig.class.getResourceAsStream("/appconfig.json")) {
                Files.copy(inputStream, configPath);
            }
        }

        try (var configFileStream = Files.newBufferedReader(configPath)) {
            return gson.fromJson(configFileStream, AppConfig.class);
        }
    }

}
