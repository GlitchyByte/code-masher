// Copyright 2020 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.mutable;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MutableTest {

    @Test
    void mutable() {
        final GMutable<List<Integer>> mutable = new GMutable<>(new ArrayList<>());
        final Collection<Integer> collection = List.of(0, 1);
        collection.forEach(value -> mutable.value.add(value));
        assertIterableEquals(collection, mutable.value);
    }

    @Test
    void mutableBoolean() {
        final GMutableBoolean mutable = new GMutableBoolean();
        final Collection<Integer> collection = List.of(0);
        collection.forEach(value -> mutable.toggle());
        assertTrue(mutable.value);
    }

    @Test
    void mutableInteger() {
        final GMutableInteger mutable = new GMutableInteger();
        final Collection<Integer> collection = List.of(5);
        collection.forEach(value -> mutable.value += value);
        assertEquals(5, mutable.value);
    }

    @Test
    void mutableLong() {
        final GMutableLong mutable = new GMutableLong();
        final Collection<Integer> collection = List.of(6);
        collection.forEach(value -> mutable.value += value);
        assertEquals(6, mutable.value);
    }

    @Test
    void mutableFloat() {
        final GMutableFloat mutable = new GMutableFloat();
        final Collection<Float> collection = List.of(11.0f);
        collection.forEach(value -> mutable.value += value);
        assertEquals(11.0f, mutable.value);
    }

    @Test
    void mutableDouble() {
        final GMutableDouble mutable = new GMutableDouble();
        final Collection<Float> collection = List.of(12.0f);
        collection.forEach(value -> mutable.value += value);
        assertEquals(12.0f, mutable.value);
    }

    @Test
    void mutableString() {
        final GMutableString mutable = new GMutableString("Hello");
        final Collection<String> collection = List.of(" world!");
        collection.forEach(value -> mutable.value += value);
        assertEquals("Hello world!", mutable.value);
    }
}
