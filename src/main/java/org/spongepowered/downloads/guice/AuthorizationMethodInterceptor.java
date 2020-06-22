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
package org.spongepowered.downloads.guice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.spongepowered.downloads.Constants;
import org.spongepowered.downloads.auth.Authentication;
import org.spongepowered.downloads.auth.annotation.Authorize;
import org.spongepowered.downloads.exception.AuthenticationFailedException;
import org.spongepowered.downloads.exception.AuthorisationFailedException;
import spark.Request;

/**
 * Intercepts {@link Authorize} decorated methods.
 */
public class AuthorizationMethodInterceptor implements MethodInterceptor {

    private final Authentication authentication;
    private final Logger logger;

    /**
     * Constructs this instance.
     *
     * @param logger The logger
     * @param authentication The {@link Authentication} instance
     */
    public AuthorizationMethodInterceptor(
            Logger logger,
            Authentication authentication) {
        this.logger = logger;
        this.authentication = authentication;
    }

    /**
     * The intercepting method.
     *
     * @param invocation The invocation of the method
     * @return The object that is returned from the annotated method
     * @throws Throwable for any exceptions that are thrown
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // We need the request object, if we don't have it, we'll just proceed.
        Request request = null;
        for (var parameter : invocation.getArguments()) {
            if (parameter instanceof Request) {
                request = (Request) parameter;
                break;
            }
        }

        // If we do not have a request object, then we proceed.
        if (request == null) {
            return invocation.proceed();
        }

        // Get the access token
        var accessToken = request.headers(Constants.X_ACCESS_TOKEN_HEADER);
        if (accessToken == null || accessToken.isEmpty()) {
            // we clearly don't have authorisation
            throw new AuthenticationFailedException();
        }

        // Check the access token
        var authSubject = this.authentication.refresh(accessToken).orElseThrow(AuthenticationFailedException::new);
        var authorize = invocation.getMethod().getAnnotation(Authorize.class);
        if (authSubject.hasPermission(authorize.value())) {
            return invocation.proceed();
        }
        throw new AuthorisationFailedException();
    }

}
