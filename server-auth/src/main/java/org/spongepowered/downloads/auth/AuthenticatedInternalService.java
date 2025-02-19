/*
 * This file is part of SystemOfADownload, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://spongepowered.org/>
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

import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.server.HeaderServiceCall;
import com.lightbend.lagom.javadsl.server.ServerServiceCall;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.lagom.javadsl.SecuredService;
import org.spongepowered.downloads.auth.utils.AuthUtils;

import java.util.function.Function;

public interface AuthenticatedInternalService extends SecuredService {

    @Override
    default <Request, Response> ServerServiceCall<Request, Response> authorize(
        final String clientName,
        final String authorizerName,
        final Function<CommonProfile, ServerServiceCall<Request, Response>> serviceCall
    ) {
        return HeaderServiceCall.compose(requestHeader ->
            requestHeader.getHeader(auth().internalHeaderKey())
                .filter(header -> header.equals(auth().internalHeaderSecret()))
                .map(verified -> new InternalApplicationProfile())
                .map(serviceCall)
                .orElseGet(() -> SecuredService.super.authorize(clientName, authorizerName, serviceCall))
        );
    }

    default <Request, Response> ServiceCall<Request, Response> authorizeInvoke(
        final ServiceCall<Request, Response> call
    ) {
        return call.handleRequestHeader(requestHeader -> requestHeader.withHeader(
            this.auth().internalHeaderKey(),
            this.auth().internalHeaderSecret()
        ));
    }

    AuthUtils auth();
}
