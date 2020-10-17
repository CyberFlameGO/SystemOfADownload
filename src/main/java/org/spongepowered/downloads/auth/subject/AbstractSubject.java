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
import java.util.EnumSet;

/**
 * A base implementation of a {@link Subject}.
 */
public abstract class AbstractSubject implements Subject {

    protected final EnumSet<Permissions> permissions;

    /**
     * Constructs this {@link AbstractSubject}.
     *
     * @param permissions The permissions for this subject
     */
    public AbstractSubject(final Collection<Permissions> permissions) {
        this.permissions = EnumSet.copyOf(permissions);
    }

    /**
     * Gets the permissions for this subject.
     *
     * @return The permissions
     */
    @Override
    public EnumSet<Permissions> getPermissions() {
        return this.permissions;
    }

    /**
     * Checks to see if the requested permissions are available for this
     * subject.
     *
     * @param permissions The permissions to check.
     * @return true if all permissions are granted.
     */
    @Override
    public boolean hasPermission(final Permissions[] permissions) {
        for (final Permissions permission : permissions) {
             if (!this.permissions.contains(permission)) {
                 return false;
             }
        }
        return true;
    }
}
