// Copyright 2020-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.wrapped;

import com.glitchybyte.glib.GStrings;

/**
 * Wrapper around a double.
 */
public class GWrappedDouble {

    /**
     * Wrapped object.
     */
    public double value;

    /**
     * Constructs a wrapper.
     *
     * @param value Initial value.
     */
    public GWrappedDouble(final double value) {
        this.value = value;
    }

    /**
     * Constructs a wrapper with an initial value of zero.
     */
    public GWrappedDouble() {
        this(0);
    }

    @Override
    public String toString() {
        return "GWrappedDouble{" +
                "value=" + GStrings.fromDouble(value) +
                '}';
    }
}
