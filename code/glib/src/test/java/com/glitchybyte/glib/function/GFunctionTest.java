// Copyright 2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.function;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GFunctionTest {

    @Test
    void canApplyOrDefault() {
        final GFunctionWithException<Integer, String> function = value -> value == 1 ? "one" : "other";
        final String result = GFunctionWithException.applyOrDefault(function, 1, "none");
        assertEquals("one", result);
    }

    @Test
    void cannotApplyOrDefault() {
        final GFunctionWithException<Integer, String> function = value -> { throw new IOException("Dummy exception!"); };
        final String result = GFunctionWithException.applyOrDefault(function, 1, "none");
        assertEquals("none", result);
    }

    @Test
    void canApplyOrNull() {
        final GFunctionWithException<Integer, String> function = value -> value == 1 ? "one" : "other";
        final String result = GFunctionWithException.applyOrNull(function, 1);
        assertEquals("one", result);
    }

    @Test
    void cannotApplyOrNull() {
        final GFunctionWithException<Integer, String> function = value -> { throw new IOException("Dummy exception!"); };
        final String result = GFunctionWithException.applyOrNull(function, 1);
        assertNull(result);
    }
}
