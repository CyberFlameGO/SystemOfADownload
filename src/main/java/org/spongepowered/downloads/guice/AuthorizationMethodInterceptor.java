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
import org.spongepowered.downloads.auth.annotation.Authorize;
import org.spongepowered.downloads.auth.subject.Subject;
import org.spongepowered.downloads.exception.AuthorisationFailedException;

/**
 * Intercepts {@link Authorize} decorated methods.
 */
public class AuthorizationMethodInterceptor implements MethodInterceptor {

    /**
     * The intercepting method.
     *
     * @param invocation The invocation of the method
     * @return The object that is returned from the annotated method
     * @throws Throwable for any exceptions that are thrown
     */
    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        // We need the request object, if we don't have it, we'll just proceed.
        Subject subject = null;
        final var method = invocation.getMethod();
        final var parameters = method.getParameters();
        var foundParameter = false;
        for (int i = 0; i < parameters.length; ++i) {
            if (Subject.class.isAssignableFrom(parameters[i].getType())) {
                subject = ((Subject) invocation.getArguments()[i]);
                foundParameter = true;
                break;
            }
        }

        // If we do not have a request object, then we proceed.
        if (!foundParameter) {
            return invocation.proceed();
        }

        // Check the access token
        final var authorize = invocation.getMethod().getAnnotation(Authorize.class);
        if (subject.hasPermission(authorize.value())) {
            return invocation.proceed();
        }
        throw new AuthorisationFailedException();
    }

}
