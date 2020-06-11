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
import com.google.inject.Injector;
import graphql.ExecutionInput;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import org.spongepowered.downloads.config.AppConfig;
import org.spongepowered.downloads.graphql.GraphQLRequest;
import org.spongepowered.downloads.guice.InjectorModule;
import spark.Spark;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    private static final String CONFIG_LOCATION = "appconfig.json";

    private final Gson gson;
    private final Injector injector;

    App() throws Exception {
        this.gson = new Gson();
        this.injector = Guice.createInjector(new InjectorModule(this.gson, load(this.gson)));
    }

    /**
     * Entrypoint for the app. Does
     *
     * @param args entrypoint args
     */
    public static void main(final String[] args) {
        final App app;
        try {
            app = new App();
        } catch (Exception e) {
            Logger.getGlobal().log(Level.SEVERE, "Unable to start server: {}", e.getMessage());
            e.printStackTrace(System.err);
            return;
        }
        Spark.post("/graphql", (request, response) -> {
            response.type("application/json");
            final String body = request.body();
            final GraphQLRequest graphQLRequest = app.gson.fromJson(body, GraphQLRequest.class);
            final GraphQLSchema.Builder schemaBuilder = GraphQLSchema.newSchema();
            final GraphQLSchema schema = schemaBuilder.build();
            final GraphQL graphQL = GraphQL.newGraphQL(schema).build();

            final ExecutionInput.Builder builder = ExecutionInput.newExecutionInput()
                .query(graphQLRequest.getQuery());
            graphQLRequest.getVariables().ifPresent(builder::variables);
            graphQLRequest.getOperationName().ifPresent(builder::operationName);
            return graphQL.execute(builder);
        });
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
