// Copyright 2020-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.combinatorics;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator class for coded sequences.
 */
public final class GCodedSequencesIterator implements Iterator<int[]> {

    private final GCodedSequencesGenerator codedGenerator;
    private int[] code;
    private final boolean[] usedIndexes;
    private int[] result;
    private int currentSize;
    private boolean isSizeCompleted;
    private boolean isCompleted = false;

    /**
     * Creates a coded sequence iterator with the characteristics described in {@code codedGenerator}.
     *
     * @param codedGenerator Describes the characteristics for this iterator.
     */
    public GCodedSequencesIterator(final GCodedSequencesGenerator codedGenerator) {
        this.codedGenerator = codedGenerator;
        usedIndexes = codedGenerator.allowDuplicateItems ? null : new boolean[codedGenerator.itemCount];
        currentSize = codedGenerator.minSize;
        reset();
    }

    private void reset() {
        code = new int[currentSize];
        if (!codedGenerator.allowDuplicateItems) {
            for (int i = 0; i < currentSize; ++i) {
                code[i] = i;
            }
        }
        result = new int[currentSize];
        isSizeCompleted = false;
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return !isCompleted;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public int[] next() {
        if (isCompleted) {
            throw new NoSuchElementException();
        }
        System.arraycopy(code, 0, result, 0, currentSize);
        final int[] currentResult = result;
        do {
            if (!codedGenerator.incCode(code)) {
                isSizeCompleted = true;
                break;
            }
        } while (!codedGenerator.allowDuplicateItems && hasDuplicateItems());
        if (isSizeCompleted) {
            ++currentSize;
            isCompleted = currentSize > codedGenerator.maxSize;
            if (!isCompleted) {
                reset();
            }
        }
        return currentResult;
    }

    private boolean hasDuplicateItems() {
        Arrays.fill(usedIndexes, false);
        for (final int index: code) {
            if (usedIndexes[index]) {
                return true;
            }
            usedIndexes[index] = true;
        }
        return false;
    }
}
