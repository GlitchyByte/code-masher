// Copyright 2020 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.mutable;

/**
 * Class to hold a single long whose container can be made final.
 */
public final class GMutableLong {

    /**
     * Mutable value.
     */
    public long value;

    /**
     * Constructs a holder.
     *
     * @param value Initial value.
     */
    public GMutableLong(final long value) {
        this.value = value;
    }

    /**
     * Constructs a holder with an initial value of zero.
     */
    public GMutableLong() {
        value = 0;
    }

    @Override
    public String toString() {
        return "mutable: " + value;
    }
}
