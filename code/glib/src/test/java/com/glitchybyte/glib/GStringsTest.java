// Copyright 2021 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GStringsTest {

    @Test
    void singleLineIndent() {
        final String input = "One line.";
        final String output = GStrings.indent(input);
        assertEquals("  One line.", output);
    }

    @Test
    void multipleLineIndent() {
        final String input = "One line.\nTwo lines.";
        final String output = GStrings.indent(input);
        assertEquals("  One line.\n  Two lines.", output);
    }

    @Test
    void formatFloat() {
        final float value = 1.9876f;
        final String output = GStrings.fromFloat(value);
        assertEquals("1.988", output);
    }

    @Test
    void formatDouble() {
        final double value = 1.9876;
        final String output = GStrings.fromDouble(value);
        assertEquals("1.988", output);
    }
}
