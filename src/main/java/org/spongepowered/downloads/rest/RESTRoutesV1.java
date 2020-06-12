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
package org.spongepowered.downloads.rest;

import com.google.inject.Inject;
import org.spongepowered.downloads.buisness.Metadata;
import org.spongepowered.downloads.rest.objects.v1.BasicProject;
import spark.Request;
import spark.Response;

import java.util.stream.Collectors;

/**
 * REST v1 routes from the previous indexer.
 */
public class RESTRoutesV1 {

    private final Metadata metadata;

    @Inject
    public RESTRoutesV1(Metadata metadata) {
        this.metadata = metadata;
    }

    /**
     * Route for GET /v1/projects.
     *
     * @param request The {@link Request}
     * @param response The {@link Response}
     * @return The response.
     */
    public Object getAllProjects(Request request, Response response) {
        setCommonResponseParameters(response);
        return this.metadata.getAllProducts().stream().map(x -> new BasicProject(x.getGroupid(), x.getArtifactid())).collect(Collectors.toList());
    }

    /**
     * Route for GET /v1/:groupId/:artifactId.
     *
     * @param request The {@link Request}
     * @param response The {@link Response}
     * @return The response.
     */
    // TODO: Implement
    public Object getProject(Request request, Response response) {
        setCommonResponseParameters(response);
        return this.metadata.getAllProducts().stream().map(x -> new BasicProject(x.getGroupid(), x.getArtifactid())).collect(Collectors.toList());
    }

    /**
     * Route for GET /v1/:groupId/:artifactId/downloads.
     *
     * @param request The {@link Request}
     * @param response The {@link Response}
     * @return The response.
     */
    // TODO: Implement
    public Object getProjectDownloads(Request request, Response response) {
        setCommonResponseParameters(response);
        return this.metadata.getAllProducts().stream().map(x -> new BasicProject(x.getGroupid(), x.getArtifactid())).collect(Collectors.toList());
    }

    /**
     * Route for GET /v1/:groupId/:artifactId/downloads/:version.
     *
     * @param request The {@link Request}
     * @param response The {@link Response}
     * @return The response.
     */
    // TODO: Implement
    public Object getProjectDownloadVersion(Request request, Response response) {
        setCommonResponseParameters(response);
        return this.metadata.getAllProducts().stream().map(x -> new BasicProject(x.getGroupid(), x.getArtifactid())).collect(Collectors.toList());
    }

    /**
     * Route for GET /v1/:groupId/:artifactId/downloads/recommended.
     *
     * @param request The {@link Request}
     * @param response The {@link Response}
     * @return The response.
     */
    // TODO: Implement
    public Object getProjectDownloadRecommended(Request request, Response response) {
        setCommonResponseParameters(response);
        return this.metadata.getAllProducts().stream().map(x -> new BasicProject(x.getGroupid(), x.getArtifactid())).collect(Collectors.toList());
    }

    private void setCommonResponseParameters(Response response) {
        response.header("Content-Type", "application/json");
        response.status(200);
    }

}
