// Copyright 2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.function;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GSupplierTest {

    @Test
    void canGetOrDefault() {
        final GSupplierWithException<String> supplier = () -> "ok";
        final String result = GSupplierWithException.getOrDefault(supplier, "maybe");
        assertEquals("ok", result);
    }

    @Test
    void cannotGetOrDefault() {
        final GSupplierWithException<String> supplier = () -> { throw new IOException("Dummy exception!"); };
        final String result = GSupplierWithException.getOrDefault(supplier, "maybe");
        assertEquals("maybe", result);
    }

    @Test
    void canGetOrNull() {
        final GSupplierWithException<String> supplier = () -> "ok";
        final String result = GSupplierWithException.getOrNull(supplier);
        assertEquals("ok", result);
    }

    @Test
    void cannotGetOrNull() {
        final GSupplierWithException<String> supplier = () -> { throw new IOException("Dummy exception!"); };
        final String result = GSupplierWithException.getOrNull(supplier);
        assertNull(result);
    }
}
