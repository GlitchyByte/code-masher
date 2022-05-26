// Copyright 2020-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.combinatorics;

/**
 * Generates permutations of the given items.
 * (Order of items do matter.)
 *
 * @param <TItem> Type of item.
 */
public class GPermutations<TItem> extends GSequencesGenerator<GCodedPermutations, TItem> {

    /**
     * Creates a sequences generator with the given characteristics.
     *
     * @param items               Items for the generator.
     * @param allowDuplicateItems {@code true} if a sequence can use the same item more than once.
     * @param minSize             Minimum size of a sequence.
     * @param maxSize             Maximum size of a sequence.
     */
    public GPermutations(final TItem[] items, final boolean allowDuplicateItems, final int minSize, final int maxSize) {
        super(new GCodedPermutations(items.length, allowDuplicateItems, minSize, maxSize), items);
    }

    /**
     * Creates a sequences generator with the given characteristics.
     *
     * @param items               Items for the generator.
     * @param allowDuplicateItems {@code true} if a sequence can use the same item more than once.
     */
    public GPermutations(final TItem[] items, final boolean allowDuplicateItems) {
        this(items, allowDuplicateItems, items.length, items.length);
    }
}
