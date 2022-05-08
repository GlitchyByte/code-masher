// Copyright 2021 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.json;

/**
 * Parses and stores a resource location.
 * <p>
 * A resource location is represented by segments and name.
 * Segments are a way to access deep names.
 * Name is the actual key we want to access.
 */
final class ResourceLocation {

    /**
     * Segment of path on the way to name.
     */
    public static final class Segment {

        /**
         * Segment name.
         */
        public final String name;

        /**
         * If name is a list, this is the index we want to access.
         * If index is NULL, it means this segment is not a list.
         */
        public final Integer index;

        /**
         * Constructs a {@code Segment} with the given name and index.
         *
         * @param name Segment name.
         * @param index Index of item in list Segment. NULL if this segment is not a list.
         */
        public Segment(final String name, final Integer index) {
            this.name = name;
            this.index = index;
        }

        /**
         * Tests if this segment is a list.
         *
         * @return True if this segment is a list.
         */
        public boolean isList() {
            return index != null;
        }
    }

    /**
     * Segments to resource.
     */
    public final Segment[] segments;

    /**
     * Resource name.
     */
    public final String name;

    /**
     * Parses {@code path} and populates a {@code ResourceLocation}.
     *
     * @param path Path to parse.
     */
    public ResourceLocation(final String path) {
        final String[] parts = path.split("/");
        final int lastPartIndex = parts.length - 1;
        segments = new Segment[lastPartIndex];
        for (int i = 0; i < lastPartIndex; ++i) {
            final String part = parts[i];
            final int openBracketPos = part.indexOf('[');
            final int closeBracketPos = part.indexOf(']');
            if ((openBracketPos == -1) && (closeBracketPos == -1)) {
                segments[i] = new Segment(part, null);
            } else if ((openBracketPos > 0) && (closeBracketPos > openBracketPos)) {
                final String name = part.substring(0, openBracketPos);
                final Integer index = Integer.valueOf(part.substring(openBracketPos + 1, closeBracketPos));
                segments[i] = new Segment(name, index);
            } else {
                throw new IllegalArgumentException("Syntax error in path: " + path);
            }
        }
        name = parts[lastPartIndex];
    }
}
