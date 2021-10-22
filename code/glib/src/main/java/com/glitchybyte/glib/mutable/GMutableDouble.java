// Copyright 2020 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.mutable;

import com.glitchybyte.glib.GStrings;

/**
 * Class to hold a single double whose container can be made final.
 */
public final class GMutableDouble {

    /**
     * Mutable value.
     */
    public double value;

    /**
     * Constructs a holder.
     *
     * @param value Initial value.
     */
    public GMutableDouble(final double value) {
        this.value = value;
    }

    /**
     * Constructs a holder with an initial value of zero.
     */
    public GMutableDouble() {
        value = 0;
    }

    @Override
    public String toString() {
        return "mutable: " + GStrings.fromDouble(value);
    }
}
