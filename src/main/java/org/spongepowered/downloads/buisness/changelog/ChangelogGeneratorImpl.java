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
package org.spongepowered.downloads.buisness.changelog;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.PersonIdent;
import org.spongepowered.downloads.config.AppConfig;
import org.spongepowered.downloads.pojo.data.Changelog;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneOffset;

/**
 * Implementation for generating a changelog.
 */
public class ChangelogGeneratorImpl implements ChangelogGenerator {

    private final AppConfig appConfig;

    /**
     * Creates this changelog generator.
     *
     * @param appConfig The config
     */
    @Inject
    public ChangelogGeneratorImpl(final AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Changelog getChangelogFor(final String productid, final String fromHash, final String toHash) {
        final AppConfig.Product product = this.appConfig.getProduct(productid);
        if (product == null) {
            throw new IllegalArgumentException("The product " + productid + " does not exist.");
        }
        return this.getChangelogFor(product, productid, fromHash, toHash);
    }

    private Changelog getChangelogFor(
            final AppConfig.Product product, final String productid, final String fromHash, final String toHash) {
        final Path productRepo = this.appConfig.getRepoCacheDirectory().resolve(productid);
        final Git gitRepo;
        try {
            if (Files.notExists(productRepo)) {
                Files.createDirectories(productRepo);
                gitRepo = Git.cloneRepository()
                        .setCloneAllBranches(true)
                        .setDirectory(productRepo.toFile())
                        .setURI(product.getRepo())
                        .call();
            } else {
                gitRepo = Git.open(productRepo.toFile());
                gitRepo.fetch().call();
            }
            final var fromCommit = ObjectId.fromString(fromHash);
            final var toCommit = ObjectId.fromString(toHash);
            final var logCommits = gitRepo.log().addRange(fromCommit, toCommit).call();
            // TODO: Submodules
            final ImmutableList.Builder<Changelog.Entry> entries = ImmutableList.builder();
            for (final var commit : logCommits) {
                final PersonIdent ident = commit.getAuthorIdent();
                entries.add(new Changelog.Entry(
                        commit.toObjectId().toString(),
                        commit.getFullMessage(),
                        ident.getName(),
                        ident.getWhen().toInstant().atZone(ZoneOffset.UTC)
                ));
            }
            return new Changelog(entries.build(), ImmutableMap.of());
        } catch (final Exception e) {
            throw new IllegalStateException("Could not get changelog.", e);
        }
    }

}
