// Copyright 2020-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.wrapped;

/**
 * Wrapper around a boolean.
 */
public class GWrappedBoolean {

    /**
     * Wrapped object.
     */
    public boolean value;

    /**
     * Constructs a wrapper.
     *
     * @param value Initial value.
     */
    public GWrappedBoolean(final boolean value) {
        this.value = value;
    }

    /**
     * Constructs a wrapper with an initial value of false.
     */
    public GWrappedBoolean() {
        this(false);
    }

    /**
     * Toggles the boolean value.
     */
    public void toggle() {
        value = !value;
    }

    @Override
    public String toString() {
        return "GWrappedBoolean{" +
                "value=" + value +
                '}';
    }
}
