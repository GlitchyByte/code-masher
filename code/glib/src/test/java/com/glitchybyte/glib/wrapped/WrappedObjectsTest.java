// Copyright 2020-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.wrapped;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WrappedObjectsTest {

    @Test
    void wrappedObject() {
        final GWrappedObject<List<Integer>> wrapped = new GWrappedObject<>(new ArrayList<>());
        final List<Integer> list = List.of(0, 1, 9);
        list.forEach(value -> wrapped.value.add(value));
        assertIterableEquals(list, wrapped.value);
    }

    @Test
    void wrappedBoolean() {
        final GWrappedBoolean wrapped = new GWrappedBoolean();
        wrapped.toggle();
        assertTrue(wrapped.value);
    }

    @Test
    void wrappedInteger() {
        final GWrappedInteger wrapped = new GWrappedInteger();
        final Collection<Integer> collection = List.of(5, 2);
        collection.forEach(value -> wrapped.value += value);
        assertEquals(7, wrapped.value);
    }

    @Test
    void wrappedLong() {
        final GWrappedLong wrapped = new GWrappedLong();
        final Collection<Long> collection = List.of(6L, 3L);
        collection.forEach(value -> wrapped.value += value);
        assertEquals(9L, wrapped.value);
    }

    @Test
    void wrappedFloat() {
        final GWrappedFloat wrapped = new GWrappedFloat();
        final Collection<Float> collection = List.of(11.0f, 4.0f);
        collection.forEach(value -> wrapped.value += value);
        assertEquals(15.0f, wrapped.value);
    }

    @Test
    void wrappedDouble() {
        final GWrappedDouble wrapped = new GWrappedDouble();
        final Collection<Float> collection = List.of(12.0f, 5.0f);
        collection.forEach(value -> wrapped.value += value);
        assertEquals(17.0f, wrapped.value);
    }

    @Test
    void wrappedString() {
        final GWrappedString wrapped = new GWrappedString("Hello");
        final List<String> list = List.of(" ", "world!");
        list.forEach(value -> wrapped.value += value);
        assertEquals("Hello world!", wrapped.value);
    }
}
