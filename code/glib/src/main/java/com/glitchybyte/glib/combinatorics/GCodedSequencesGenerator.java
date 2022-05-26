// Copyright 2020 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.combinatorics;

import java.util.Iterator;

/**
 * Abstract coded sequence generator.
 * <p>
 * The code itself is a sequence of indexes.
 */
public abstract class GCodedSequencesGenerator implements Iterable<int[]> {

    /**
     * Number of items.
     */
    public final int itemCount;

    /**
     * Whether to allow duplicate items in a sequence.
     */
    public final boolean allowDuplicateItems;

    /**
     * Minimum size of a sequence.
     */
    public final int minSize;

    /**
     * Maximum size of a sequence.
     */
    public final int maxSize;

    /**
     * Creates a coded sequence generator with an iterator with the given characteristics.
     *
     * @param itemCount           Number of unique items.
     * @param allowDuplicateItems {@code true} if a sequence can use the same item more than once.
     * @param minSize             Minimum size of a sequence.
     * @param maxSize             Maximum size of a sequence.
     */
    public GCodedSequencesGenerator(final int itemCount, final boolean allowDuplicateItems,
                                    final int minSize, final int maxSize) {
        if (itemCount <= 0) {
            throw new IllegalArgumentException("itemCount must be greater than zero.");
        }
        if (minSize <= 0) {
            throw new IllegalArgumentException("minSize must be greater than zero.");
        }
        if (maxSize < minSize) {
            throw new IllegalArgumentException("maxSize must be greater than or equal to minSize.");
        }
        if (!allowDuplicateItems && (maxSize > itemCount)) {
            throw new IllegalArgumentException("Not allowing duplicate items, maxSize must be less than or equal to itemCount.");
        }
        this.itemCount = itemCount;
        this.allowDuplicateItems = allowDuplicateItems;
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    /**
     * Creates a coded sequence generator of a size equal to {@code itemCount}.
     *
     * @param itemCount           Number of unique items.
     * @param allowDuplicateItems {@code true} if a sequence can use the same item more than once.
     */
    public GCodedSequencesGenerator(final int itemCount, final boolean allowDuplicateItems) {
        this(itemCount, allowDuplicateItems, itemCount, itemCount);
    }

    /**
     * Returns an iterator over elements of type {@code int[]}.
     *
     * @return Iterator.
     */
    @Override
    public Iterator<int[]> iterator() {
        return new GCodedSequencesIterator(this);
    }

    /**
     * Increases the code one step.
     * <p>
     * Implementations do not need to worry about {@code allowDuplicateItems}. The iterator will check that and
     * requests another one as necessary.
     *
     * @param code Current code.
     * @return {@code true} if code was increased.
     */
    protected abstract boolean incCode(final int[] code);
}
