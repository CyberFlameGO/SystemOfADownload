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
package org.spongepowered.downloads.auth.provider;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.downloads.auth.subject.AuthSubject;
import org.spongepowered.downloads.auth.subject.Permissions;
import org.spongepowered.downloads.exception.AuthenticationFailedException;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Random;

/**
 * A dummy auth class to test with.
 */
public class DummyOAuthAuthenticationProvider implements OAuthAuthenticationProvider {

    @Nullable private AuthSubject token;

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthSubject authenticate(String token) throws AuthenticationFailedException {
        var byteArray = new byte[32];
        new Random().nextBytes(byteArray);
        final StringBuilder builder = new StringBuilder();
        for (var b : byteArray) {
            builder.append(String.format("%02x", b));
        }
        this.token = new AuthSubject(
                "testuser",
                builder.toString(),
                EnumSet.allOf(Permissions.class));
        return this.token;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<AuthSubject> refresh(String accessToken) {
        if (this.isTokenValid(accessToken)) {
            return Optional.ofNullable(this.token);
        }
        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroyToken(String accessToken) {
        if (this.isTokenValid(accessToken)) {
            this.token = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTokenValid(String accessToken) {
        return this.token != null && this.token.getAccessToken().equals(accessToken);
    }

}
