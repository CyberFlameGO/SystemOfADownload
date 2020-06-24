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
package org.spongepowered.downloads.pojo.data;

/**
 * Represents a project.
 */
public class Project {

    private final String groupId;
    private final String artifactId;
    private final String id;

    /**
     * Constructs this instance.
     *
     * @param groupId The group ID of the project
     * @param artifactId The artifact ID of the project
     * @param id The ID of the project
     */
    public Project(String groupId, String artifactId, String id) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.id = id;
    }

    /**
     * Gets the group ID.
     *
     * @return The group ID
     */
    public String getGroupId() {
        return this.groupId;
    }

    /**
     * Gets the artifact ID.
     *
     * @return The artifact ID
     */
    public String getArtifactId() {
        return this.artifactId;
    }

    /**
     * The ID of the project.
     *
     * @return The ID
     */
    public String getId() {
        return this.id;
    }

}
