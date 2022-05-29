// Copyright 2020-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.wrapped;

/**
 * Wrapper around a long.
 */
public class GWrappedLong {

    /**
     * Wrapped object.
     */
    public long value;

    /**
     * Constructs a wrapper.
     *
     * @param value Initial value.
     */
    public GWrappedLong(final long value) {
        this.value = value;
    }

    /**
     * Constructs a wrapper with an initial value of zero.
     */
    public GWrappedLong() {
        this(0);
    }

    @Override
    public String toString() {
        return "GWrappedInteger{" +
                "value=" + value +
                '}';
    }
}
