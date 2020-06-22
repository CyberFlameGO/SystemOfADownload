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
package org.spongepowered.downloads.auth;

import org.spongepowered.downloads.exception.AuthenticationFailedException;

import java.util.Optional;

/**
 * Contains authentication methods.
 */
public interface Authentication {

    /**
     * Uses the given token to authenticate against the auth service.
     *
     * @param authToken The token provided by the OAuth authentication service
     *      to pass to the service to complete authentication.
     * @return The auth subject
     * @throws AuthenticationFailedException if authentication failed
     */
    AuthSubject authenticate(String authToken) throws AuthenticationFailedException;

    /**
     * Reobtains the {@link AuthSubject} for a given access token.
     *
     * @param accessToken The access token
     * @return The {@link AuthSubject}, if the token is still valid
     */
    Optional<AuthSubject> refresh(String accessToken);

    /**
     * Destroys the given token, effectively a "log out".
     *
     * @param accessToken The token.
     */
    void destroyToken(String accessToken);

    /**
     * Tests whether the given token is still valid.
     *
     * @param accessToken The token
     * @return if the token is valid.
     */
    boolean isTokenValid(String accessToken);

}
