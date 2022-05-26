// Copyright 2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.combinatorics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract sequences generator.
 *
 * @param <TCodedGenerator> Type of sequences generator.
 * @param <TItem> Type of item.
 */
public final class GSequencesIterator<TCodedGenerator extends GCodedSequencesGenerator, TItem>
        implements Iterator<List<TItem>> {

    private final Iterator<int[]> codedIterator;
    private final TItem[] items;

    /**
     * Creates an iterator of item sequences.
     *
     * @param generator Item sequences generator.
     */
    public GSequencesIterator(final GSequencesGenerator<TCodedGenerator, TItem> generator) {
        codedIterator = generator.codedGenerator.iterator();
        items = generator.items;
    }

    @Override
    public boolean hasNext() {
        return codedIterator.hasNext();
    }

    @Override
    public List<TItem> next() {
        final int[] code = codedIterator.next();
        final List<TItem> result = new ArrayList<>(code.length);
        for (final int index: code) {
            result.add(items[index]);
        }
        return result;
    }
}
