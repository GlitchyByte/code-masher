// Copyright 2020-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.combinatorics;

import java.util.Iterator;
import java.util.List;

/**
 * Abstract sequences generator.
 *
 * @param <TCodedGenerator> Type of coded sequences generator.
 * @param <TItem>      Type of item.
 */
public abstract class GSequencesGenerator<TCodedGenerator extends GCodedSequencesGenerator, TItem>
        implements Iterable<List<TItem>> {

    final TCodedGenerator codedGenerator;
    final TItem[] items;

    /**
     * Creates a sequences generator with the given characteristics.
     *
     * @param codedGenerator Coded sequence generator for the type of sequences we want to generate.
     * @param items Items for the generator.
     */
    public GSequencesGenerator(final TCodedGenerator codedGenerator, final TItem[] items) {
        this.codedGenerator = codedGenerator;
        this.items = items;
    }

    /**
     * Returns an iterator over elements of type {@code TItem}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<List<TItem>> iterator() {
        return new GSequencesIterator<>(this);
    }
}
