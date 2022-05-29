// Copyright 2020-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.wrapped;

import com.glitchybyte.glib.GStrings;

/**
 * Wrapper around a float.
 */
public class GWrappedFloat {

    /**
     * Wrapped object.
     */
    public float value;

    /**
     * Constructs a wrapper.
     *
     * @param value Initial value.
     */
    public GWrappedFloat(final float value) {
        this.value = value;
    }

    /**
     * Constructs a wrapper with an initial value of zero.
     */
    public GWrappedFloat() {
        this(0);
    }

    @Override
    public String toString() {
        return "GWrappedDouble{" +
                "value=" + GStrings.fromFloat(value) +
                '}';
    }
}
