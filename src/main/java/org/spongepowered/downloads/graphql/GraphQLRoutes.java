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
package org.spongepowered.downloads.graphql;

import com.google.gson.Gson;
import com.google.inject.Inject;
import graphql.ExecutionInput;
import graphql.GraphQL;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import org.spongepowered.downloads.buisness.Metadata;
import org.spongepowered.downloads.buisness.UploadProcessor;
import spark.Request;
import spark.Response;

/**
 * Contains the GraphQL routes
 */
public class GraphQLRoutes {

    private final Gson gson;
    private final Metadata metadata;
    private final UploadProcessor uploadProcessor;
    private final GraphQLSchema schema;

    /**
     * Creates this instance.
     *
     * @param gson The {@link Gson} instance for the app
     * @param metadata The {@link Metadata} business logic layer
     * @param uploadProcessor The {@link UploadProcessor} business logic layer
     */
    @Inject
    public GraphQLRoutes(Gson gson, Metadata metadata, UploadProcessor uploadProcessor) {
        this.gson = gson;
        this.metadata = metadata;
        this.uploadProcessor = uploadProcessor;
        this.schema = createV1Schema();
    }

    /**
     * Processes the GraphQL request.
     *
     * @param request The {@link Request}
     * @param response The {@link Response}
     * @return The object to return
     */
    public Object process(Request request, Response response) {
        response.type("application/json");
        final String body = request.body();
        final GraphQLRequest graphQLRequest = this.gson.fromJson(body, GraphQLRequest.class);

        final GraphQL graphQL = GraphQL.newGraphQL(this.schema).build();

        final ExecutionInput.Builder builder = ExecutionInput.newExecutionInput()
                .query(graphQLRequest.getQuery());
        graphQLRequest.getVariables().ifPresent(builder::variables);
        graphQLRequest.getOperationName().ifPresent(builder::operationName);
        return graphQL.execute(builder);
    }

    /**
     * Creates the v1 GraphQL schema for this application
     *
     * @return The {@link GraphQLSchema}
     */
    private static GraphQLSchema createV1Schema() {
        final GraphQLSchema.Builder schemaBuilder = GraphQLSchema.newSchema();
        schemaBuilder.query(GraphQLObjectType.newObject().name("test").build());
        return schemaBuilder.build();
    }

}
