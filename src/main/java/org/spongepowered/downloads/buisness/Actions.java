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
package org.spongepowered.downloads.buisness;

import org.spongepowered.downloads.auth.annotation.Authorize;
import org.spongepowered.downloads.auth.subject.Permissions;
import org.spongepowered.downloads.auth.subject.Subject;
import org.spongepowered.downloads.pojo.data.Downloadable;
import org.spongepowered.downloads.pojo.data.Project;
import org.spongepowered.downloads.pojo.query.DownloadableQuery;

import java.net.URL;
import java.util.Collection;
import java.util.Optional;

/**
 * Contains the entry points to the actions that might be executed.
 */
public interface Actions {

    /**
     * Gets all projects that this system will display.
     *
     * @return The {@link Project}s
     */
    Collection<Project> getAllProjects();

    /**
     * Gets the specified project, if it exists.
     *
     * @param projectId The ID of the project
     * @return The {@link Project}, if it exists
     */
    Optional<Project> getProject(String projectId);

    /**
     * Gets downloads associated with a query.
     *
     * @param downloadableQuery The query
     * @return The {@link Downloadable}s
     */
    Collection<Downloadable> getDownloads(DownloadableQuery downloadableQuery);

    /**
     * Adds a new build to this indexer.
     *
     * @param subject The {@link Subject} that is executing this action
     * @param projectId The project ID to add to
     * @param downloadLocation The location of the download
     * @return The new {@link Downloadable}
     */
    @Authorize(Permissions.ADD_DOWNLOAD)
    Downloadable addDownload(Subject subject, String projectId, URL downloadLocation);

    /**
     * Resyncs the specified project with what is available on Maven.
     *
     * @param subject The {@link Subject} that is executing this action
     * @param projectId The project ID to resync
     */
    @Authorize(Permissions.RESYNC)
    void resyncWithMaven(Subject subject, String projectId);

    /**
     * Marks or unmarks a download as broken.
     *
     * @param subject The {@link Subject} that is executing this action
     * @param projectId The ID of the project
     * @param downloadId The ID of the download
     * @param isBroken Whether the download is broken or not
     */
    @Authorize(Permissions.MARK_BROKEN)
    void markBroken(Subject subject, String projectId, int downloadId, boolean isBroken);

    /**
     * Updates the changelog of a given build.
     *
     * @param subject The {@link Subject} that is executing this action
     * @param projectId The ID of the project
     * @param downloadId The ID of the download
     * @param changelog The changelog
     */
    @Authorize(Permissions.EDIT_CHANGELOG)
    void editChangelog(Subject subject, String projectId, int downloadId, String changelog);

}
