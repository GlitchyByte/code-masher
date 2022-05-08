// Copyright 2021 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void base64Encode() {
        final String message = "Obfuscate this";
        final String encoded = GStrings.base64Encode(message);
        assertEquals("T2JmdXNjYXRlIHRoaXM=", encoded);
    }

    @Test
    void base64Decode() {
        final String encoded = "T2JmdXNjYXRlIHRoaXM=";
        final String message = GStrings.base64Decode(encoded);
        assertEquals("Obfuscate this", message);
    }
}
