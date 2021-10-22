// Copyright 2020 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.mutable;

/**
 * Class to hold a single boolean whose container can be made final.
 */
public final class GMutableBoolean {

    /**
     * Mutable value.
     */
    public boolean value;

    /**
     * Constructs a holder.
     *
     * @param value Initial value.
     */
    public GMutableBoolean(final boolean value) {
        this.value = value;
    }

    /**
     * Constructs a holder with an initial value of false.
     */
    public GMutableBoolean() {
        value = false;
    }

    /**
     * Toggles the boolean value.
     */
    public void toggle() {
        value = !value;
    }

    @Override
    public String toString() {
        return "mutable: " + value;
    }
}
