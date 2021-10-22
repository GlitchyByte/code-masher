// Copyright 2020 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.mutable;

import com.glitchybyte.glib.GStrings;

/**
 * Class to hold a single float whose container can be made final.
 */
public final class GMutableFloat {

    /**
     * Mutable value.
     */
    public float value;

    /**
     * Constructs a holder.
     *
     * @param value Initial value.
     */
    public GMutableFloat(final float value) {
        this.value = value;
    }

    /**
     * Constructs a holder with an initial value of zero.
     */
    public GMutableFloat() {
        value = 0;
    }

    @Override
    public String toString() {
        return "mutable: " + GStrings.fromFloat(value);
    }
}
