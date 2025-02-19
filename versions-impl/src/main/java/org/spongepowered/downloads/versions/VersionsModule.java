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
package org.spongepowered.downloads.versions;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.lightbend.lagom.javadsl.api.ServiceLocator;
import com.lightbend.lagom.javadsl.client.ConfigurationServiceLocator;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import org.pac4j.core.config.Config;
import org.spongepowered.downloads.artifact.api.ArtifactService;
import org.spongepowered.downloads.auth.SOADAuth;
import org.spongepowered.downloads.auth.utils.AuthUtils;
import org.spongepowered.downloads.versions.api.VersionsService;
import org.spongepowered.downloads.versions.readside.VersionReadSidePersistence;
import play.Environment;

public class VersionsModule extends AbstractModule implements ServiceGuiceSupport {

    private final Environment environment;
    private final com.typesafe.config.Config config;
    private final AuthUtils auth;

    public VersionsModule(final Environment environment, final com.typesafe.config.Config config) {
        this.environment = environment;
        this.config = config;
        this.auth = AuthUtils.configure(config);
    }

    @Override
    protected void configure() {
        if (this.environment.isProd()) {
            this.bind(ServiceLocator.class).to(ConfigurationServiceLocator.class);
        }
        this.bindService(VersionsService.class, VersionsServiceImpl.class);
        this.bindClient(ArtifactService.class);

        this.bind(VersionReadSidePersistence.class).asEagerSingleton();
    }

    @Provides
    @SOADAuth
    protected Config configProvider() {
        return this.auth.config();
    }

    @Provides
    protected AuthUtils authProvider() {
        return this.auth;
    }

}
