// Copyright 2020-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.combinatorics;

/**
 * Generates combinations of the given items.
 * (Order of items do not matter.)
 *
 * @param <TItem> Type of item.
 */
public class GCombinations<TItem> extends GSequencesGenerator<GCodedCombinations, TItem> {

    /**
     * Creates a sequences generator with the given characteristics.
     *
     * @param items               Items for the generator.
     * @param allowDuplicateItems {@code true} if a sequence can use the same item more than once.
     * @param minSize             Minimum size of a sequence.
     * @param maxSize             Maximum size of a sequence.
     */
    public GCombinations(final TItem[] items, final boolean allowDuplicateItems, final int minSize, final int maxSize) {
        super(new GCodedCombinations(items.length, allowDuplicateItems, minSize, maxSize), items);
    }

    /**
     * Creates a sequences generator with the given characteristics.
     *
     * @param items               Items for the generator.
     * @param allowDuplicateItems {@code true} if a sequence can use the same item more than once.
     */
    public GCombinations(final TItem[] items, final boolean allowDuplicateItems) {
        this(items, allowDuplicateItems, items.length, items.length);
    }
}
