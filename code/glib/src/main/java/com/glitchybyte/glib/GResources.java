// Copyright 2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Java resource handling utilities.
 */
public final class GResources {

    /**
     * Retrieves an {@link InputStream} for the given resource name.
     *
     * @param context Any object within the context of the resource.
     * @param resourceName Resource name.
     * @return An {@link InputStream} for the given resource name.
     */
    public static InputStream getResourceInputStream(final Object context, final String resourceName) {
        return context.getClass().getClassLoader().getResourceAsStream(resourceName);
    }

    /**
     * Retrieves a resource as a {@link String}.
     *
     * @param context Any object within the context of the resource.
     * @param resourceName Resource name.
     * @return A resource as a {@link String}.
     * @throws IOException If an IO error occurs.
     */
    public static String getResourceString(final Object context, final String resourceName) throws IOException {
        final InputStream inputStream = getResourceInputStream(context, resourceName);
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }

    private GResources() {
        // Hiding constructor.
    }
}
