// Copyright 2018-2020 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.mutable;

/**
 * Class to hold a single int whose container can be made final.
 */
public final class GMutableInteger {

    /**
     * Mutable value.
     */
    public int value;

    /**
     * Constructs a holder.
     *
     * @param value Initial value.
     */
    public GMutableInteger(final int value) {
        this.value = value;
    }

    /**
     * Constructs a holder with an initial value of zero.
     */
    public GMutableInteger() {
        value = 0;
    }

    @Override
    public String toString() {
        return "mutable: " + value;
    }
}
