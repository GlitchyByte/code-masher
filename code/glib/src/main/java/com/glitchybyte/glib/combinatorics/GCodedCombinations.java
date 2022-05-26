// Copyright 2020 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.combinatorics;

/**
 * Coded combinations.
 * <p>
 * A coded sequence generator where the order of items do not matter.
 * The code itself is a sequence of indexes.
 */
public final class GCodedCombinations extends GCodedSequencesGenerator {

    /**
     * Creates a coded sequence generator with an iterator with the given characteristics.
     *
     * @param itemCount           Number of unique items.
     * @param allowDuplicateItems {@code true} if a sequence can use the same item more than once.
     * @param minSize             Minimum size of a sequence.
     * @param maxSize             Maximum size of a sequence.
     */
    public GCodedCombinations(final int itemCount, final boolean allowDuplicateItems, final int minSize, final int maxSize) {
        super(itemCount, allowDuplicateItems, minSize, maxSize);
    }

    /**
     * Creates a coded sequence generator of a size equal to {@code itemCount}.
     *
     * @param itemCount           Number of unique items.
     * @param allowDuplicateItems {@code true} if a sequence can use the same item more than once.
     */
    public GCodedCombinations(final int itemCount, final boolean allowDuplicateItems) {
        super(itemCount, allowDuplicateItems);
    }

    /**
     * Increases the code one step.
     *
     * @param code Current code.
     * @return {@code true} if code was increased.
     */
    @Override
    protected boolean incCode(final int[] code) {
        // Increase from right to left.
        final int currentSize = code.length;
        final int maxCount = itemCount - 1;
        for (int i = currentSize - 1; i >= 0; --i) {
            if (code[i] < maxCount) {
                ++code[i];
                if (allowDuplicateItems) {
                    for (int j = i + 1; j < currentSize; ++j) {
                        code[j] = code[i];
                    }
                } else {
                    for (int j = i + 1; j < currentSize; ++j) {
                        code[j] = code[j - 1] + 1;
                        if (code[j] > maxCount) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }
}
