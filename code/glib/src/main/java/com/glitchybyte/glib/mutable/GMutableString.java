// Copyright 2020 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.mutable;

/**
 * Class to hold a single String whose container can be made final.
 */
public final class GMutableString {

    /**
     * Mutable value.
     */
    public String value;

    /**
     * Constructs a holder.
     *
     * @param value Initial value.
     */
    public GMutableString(final String value) {
        this.value = value;
    }

    /**
     * Constructs a holder with an initial value of null.
     */
    public GMutableString() {
        value = null;
    }

    @Override
    public String toString() {
        return "mutable: " + value;
    }
}
