// Copyright 2021-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

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
    @Order(1)
    void willWaitABit() throws InterruptedException {
        final long startTime = System.currentTimeMillis();
        final Object obj = new Object();
        GObjects.hold(obj, 300);
        final long now = System.currentTimeMillis();
        assertTrue((now - startTime) >= 300);
    }

    @Test
    @Order(2)
    void willWaitForever() throws InterruptedException {
        final Thread thread = new Thread(() -> {
            final Object obj = new Object();
            try {
                GObjects.hold(obj);
            } catch (final InterruptedException e) {
                // No-op.
            }
        });
        thread.start();
        final Object waiter = new Object();
        GObjects.hold(waiter, 300);
        assertTrue(thread.isAlive());
        thread.interrupt();
    }

    private static final class BusyThread extends Thread {

        public BusyThread(final AtomicBoolean isDone, final long timeoutMillis) {
            super(() -> {
                try {
                    synchronized (isDone) {
                        isDone.wait(timeoutMillis);
                        isDone.set(true);
                        isDone.notifyAll();
                    }
                } catch (final InterruptedException e) {
                    // No-op.
                }
            });
        }
    }

    @Test
    @Order(3)
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    void willWaitABitWithCondition() throws InterruptedException {
        final AtomicBoolean isDone = new AtomicBoolean(false);
        final Thread thread = new BusyThread(isDone, 100);
        thread.start();
        try {
            synchronized (isDone) {
                assertTrue(GObjects.waitWithCondition(isDone, 200, isDone::get));
            }
        } finally {
            thread.interrupt();
        }
    }

    @Test
    @Order(4)
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    void willWaitABitWithConditionTimeout() throws InterruptedException {
        final AtomicBoolean isDone = new AtomicBoolean(false);
        final Thread thread = new BusyThread(isDone, 200);
        thread.start();
        try {
            synchronized (isDone) {
                assertFalse(GObjects.waitWithCondition(isDone, 100, isDone::get));
            }
        } finally {
            thread.interrupt();
        }
    }

    @Test
    @Order(5)
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    void willWaitForeverWithCondition() throws InterruptedException {
        final AtomicBoolean isDone = new AtomicBoolean(false);
        final Thread thread = new BusyThread(isDone, 200);
        thread.start();
        try {
            synchronized (isDone) {
                GObjects.waitWithCondition(isDone, isDone::get);
            }
            assertTrue(isDone.get());
        } finally {
            thread.interrupt();
        }
    }
}
