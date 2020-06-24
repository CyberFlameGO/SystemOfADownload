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
import org.spongepowered.downloads.buisness.Actions;
import org.spongepowered.downloads.exception.NotFoundException;
import org.spongepowered.downloads.pojo.data.Downloadable;
import org.spongepowered.downloads.pojo.data.Project;
import org.spongepowered.downloads.pojo.query.DownloadableQuery;
import org.spongepowered.downloads.rest.objects.v1.BasicProject;
import org.spongepowered.downloads.rest.objects.v1.Download;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST v1 routes from the previous indexer.
 */
public class RESTRoutesV1 {

    private final Actions actions;

    /**
     * Creates this instance.
     *
     * @param actions The {@link Actions} business layer
     */
    @Inject
    public RESTRoutesV1(Actions actions) {
        this.actions = actions;

        Spark.path("/v1", () -> {
            // Temporary route for testing the route works.
            Spark.get("/greet", ((request, response) -> "Hello"));

            Spark.get("/projects", this::getAllProjects);
            Spark.get("/:groupId/:artifactId", this::getProject);
            Spark.get("/:groupId/:artifactId/downloads",
                    this::getProjectDownloads);
            Spark.get("/:groupId/:artifactId/downloads/:version",
                    this::getProjectDownloadVersion);
            Spark.get("/:groupId/:artifactId/downloads/recommended",
                    this::getProjectDownloadRecommended);
        });
    }

    /**
     * Route for GET /v1/projects.
     *
     * @param request The {@link Request}
     * @param response The {@link Response}
     * @return The response.
     */
    public Collection<BasicProject> getAllProjects(Request request, Response response) {
        setCommonResponseParameters(response);
        return this.actions.getAllProjects().stream().map(this::translate).collect(Collectors.toList());
    }

    /**
     * Route for GET /v1/:groupId/:artifactId.
     *
     * @param request The {@link Request}
     * @param response The {@link Response}
     * @return The response.
     */
    public BasicProject getProject(Request request, Response response) {
        setCommonResponseParameters(response);
        return getProject(request)
                .map(this::translate)
                .orElseThrow(NotFoundException::new);
    }

    /**
     * Route for GET /v1/:groupId/:artifactId/downloads.
     *
     * @param request The {@link Request}
     * @param response The {@link Response}
     * @return The response.
     */
    // type=stable&version=5&minecraft=1.10.2&forge=13.19.0.2157&limit=100&until=&since=&changelog=
    public Collection<Download> getProjectDownloads(Request request, Response response) {
        setCommonResponseParameters(response);
        var project = getProject(request).orElseThrow(NotFoundException::new);
        return this.actions.getDownloads(new DownloadableQuery(project.getId(), null, false)).stream()
                .map(this::translate).collect(Collectors.toList());
    }

    /**
     * Route for GET /v1/:groupId/:artifactId/downloads/:version.
     *
     * @param request The {@link Request}
     * @param response The {@link Response}
     * @return The response.
     */
    public Download getProjectDownloadVersion(Request request, Response response) {
        setCommonResponseParameters(response);
        var project = getProject(request).orElseThrow(NotFoundException::new);
        var version = request.params(":version");
        return this.actions.getDownloads(new DownloadableQuery(project.getId(), version, false)).stream()
                .map(this::translate).findFirst().orElseThrow(NotFoundException::new);
    }

    /**
     * Route for GET /v1/:groupId/:artifactId/downloads/recommended.
     *
     * @param request The {@link Request}
     * @param response The {@link Response}
     * @return The response.
     */
    // type=stable&version=5&minecraft=1.10.2&forge=13.19.0.2157
    // TODO: Implement properly
    public Download getProjectDownloadRecommended(Request request, Response response) {
        setCommonResponseParameters(response);
        var project = getProject(request).orElseThrow(NotFoundException::new);
        return this.actions.getDownloads(new DownloadableQuery(project.getId(), null, true)).stream()
                .map(this::translate).findFirst().orElseThrow(NotFoundException::new);
    }

    private void setCommonResponseParameters(Response response) {
        response.header("Content-Type", "application/json");
        response.status(200);
    }

    private Optional<Project> getProject(Request request) {
        var groupId = request.params(":groupId");
        var artifactId = request.params(":artifactId");
        return this.actions
                .getAllProjects()
                .stream()
                .filter(x -> x.getGroupId().equals(groupId) && x.getArtifactId().equals(artifactId))
                .findFirst();
    }

    private BasicProject translate(Project project) {
        return new BasicProject(project.getGroupId(), project.getArtifactId());
    }

    private Download translate(Downloadable downloadable) {
        return new Download(downloadable);
    }

}
