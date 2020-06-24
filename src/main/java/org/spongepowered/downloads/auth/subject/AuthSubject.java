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
package org.spongepowered.downloads.auth.subject;

import java.util.Collection;

/**
 * Represents the data and permissions received from the Auth service.
 */
public final class AuthSubject extends AbstractSubject {

    private final String userid;
    private final String accessToken;

    /**
     * Constructs this AuthSubject.
     *
     * @param userid The user ID
     * @param accessToken The user's authorisation token
     * @param permissions The set of permissions they have
     */
    public AuthSubject(String userid, String accessToken, Collection<Permissions> permissions) {
        super(permissions);
        this.userid = userid;
        this.accessToken = accessToken;
    }

    /**
     * Gets the User ID of this user.
     *
     * @return The user ID
     */
    public String getUserid() {
        return this.userid;
    }

    /**
     * Gets the user's auth token.
     *
     * @return The user auth token
     */
    public String getAccessToken() {
        return this.accessToken;
    }

}
