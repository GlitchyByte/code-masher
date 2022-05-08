// Copyright 2021 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

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
}
