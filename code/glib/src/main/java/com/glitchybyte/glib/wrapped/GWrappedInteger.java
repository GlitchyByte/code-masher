// Copyright 2018-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.wrapped;

/**
 * Wrapper around an int.
 */
public class GWrappedInteger {

    /**
     * Wrapped object.
     */
    public int value;

    /**
     * Constructs a wrapper.
     *
     * @param value Initial value.
     */
    public GWrappedInteger(final int value) {
        this.value = value;
    }

    /**
     * Constructs a wrapper with an initial value of zero.
     */
    public GWrappedInteger() {
        this(0);
    }

    @Override
    public String toString() {
        return "GWrappedInteger{" +
                "value=" + value +
                '}';
    }
}
