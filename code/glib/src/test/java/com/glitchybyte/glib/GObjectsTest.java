// Copyright 2021-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib;

import com.glitchybyte.glib.function.GSupplierWithException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class GObjectsTest {

    private static class Animal {

        public final String name;

        public Animal(final String name) {
            this.name = name;
        }
    }

    private static class Horse extends Animal {

        public final String color;

        public Horse(final String name, final String color) {
            super(name);
            this.color = color;
        }
    }

    private static class Bird extends Animal {

        public final int wingspan;

        public Bird(final String name, final int wingspan) {
            super(name);
            this.wingspan = wingspan;
        }
    }

    @Test
    void canCast() {
        final Horse horse = new Horse("Joergen", "Gray");
        final Animal animal = GObjects.castOrNull(horse, Animal.class);
        assertNotNull(animal);
    }

    @Test
    void cannotCast() {
        final Horse horse = new Horse("Joergen", "Gray");
        final Bird bird = GObjects.castOrNull(horse, Bird.class);
        assertNull(bird);
    }

    @Test
    void canSupplyObject() {
        final GSupplierWithException<String> supplier = () -> "ok";
        final String result = GObjects.suppliedObjectOrNull(supplier);
        assertEquals("ok", result);
    }

    @Test
    void cannotSupplyObject() {
        final GSupplierWithException<String> supplier = () -> { throw new IOException("Dummy exception!"); };
        final String result = GObjects.suppliedObjectOrNull(supplier);
        assertNull(result);
    }
}
