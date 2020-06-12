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

// TODO: There will be more to add here, for sure.
/**
 * Represents an uploaded file.
 */
public class UploadedArtifact {

    private final String productid;
    private final String name;
    private final String version;
    private final String commit;
    private final byte[] file;

    /**
     * Constructs this uploaded artifact.
     *
     * @param productid The product ID.
     * @param name The name of the artifact
     * @param version The version of the artifact
     * @param commit The commit this file was created with
     * @param file The file
     */
    public UploadedArtifact(String productid, String name, String version, String commit, byte[] file) {
        this.productid = productid;
        this.name = name;
        this.version = version;
        this.file = file;
        this.commit = commit;
    }

    /**
     * The product ID.
     *
     * @return The product ID
     */
    public String getProductID() {
        return this.productid;
    }

    /**
     * The name of the file.
     *
     * @return The name
     */
    public String getName() {
        return this.name;
    }

    /**
     * The version of the file.
     *
     * @return The version
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * The git commit hash.
     *
     * @return the commit
     */
    public String getCommit() {
        return this.commit;
    }

    /**
     * The file.
     *
     * @return The array of bytes that represets this file
     */
    public byte[] getFile() {
        return this.file;
    }

}
