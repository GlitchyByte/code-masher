// Copyright 2014-2020 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.mutable;

/**
 * Class to hold a single object whose container can be made final.
 *
 * @param <T> Type of object being held.
 */
public final class GMutable<T> {

    /**
     * Mutable value.
     */
    public T value;

    /**
     * Constructs a holder.
     *
     * @param value Initial value.
     */
    public GMutable(final T value) {
        this.value = value;
    }

    /**
     * Constructs a holder with an initial value of null.
     */
    public GMutable() {
        value = null;
    }

    /**
     * Tests if holder is holding a null.
     *
     * @return True if null.
     */
    public boolean isNull() {
        return value == null;
    }

    @Override
    public String toString() {
        return "mutable: " + (isNull() ? "" : value.toString());
    }
}
