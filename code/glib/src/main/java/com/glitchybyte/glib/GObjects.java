// Copyright 2014-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib;

import java.time.Duration;
import java.time.Instant;
import java.util.function.BooleanSupplier;

/**
 * Object utilities.
 */
public final class GObjects {

    /**
     * Test if {@code obj} is of type {@code tClass} and returns the same object of that type.
     * If it's not of the type, it returns null.
     *
     * @param obj Object to test and return.
     * @param tClass Class we want the object to be.
     * @param <T> Type of returned object.
     * @return {@code obj} cast as {@code tClass} or null.
     */
    public static <T> T castOrNull(final Object obj, final Class<T> tClass) {
        if (obj == null) {
            return null;
        }
        return tClass.isInstance(obj) ? tClass.cast(obj) : null;
    }

    /**
     * Waits on an object to be notified, interrupted, or until timeout.
     *
     * @param obj Object to wait on.
     * @param timeoutMillis Timeout in milliseconds, or zero to wait forever.
     *
     * @throws InterruptedException If thread is interrupted.
     */
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public static void hold(final Object obj, final long timeoutMillis) throws InterruptedException {
        synchronized (obj) {
            obj.wait(timeoutMillis);
        }
    }

    /**
     * Waits forever on an object to be notified or interrupted.
     *
     * @param obj Object to wait on.
     *
     * @throws InterruptedException If thread is interrupted.
     */
    public static void hold(final Object obj) throws InterruptedException {
        hold(obj, 0);
    }

    /**
     * Waits on object until {@code endCondition} returns true.
     *
     * <p>Test happens before waiting, so if condition is true it will return immediately and never wait.
     *
     * @param obj Object to wait on.
     * @param endCondition End condition to test.
     *
     * @throws InterruptedException If thread is interrupted.
     */
    public static void waitWithCondition(final Object obj, final BooleanSupplier endCondition) throws InterruptedException {
        while (!endCondition.getAsBoolean()) {
            obj.wait();
        }
    }

    /**
     * Waits on object until {@code endCondition} returns true, or it times out.
     *
     * <p>Test happens before waiting, so if condition is true it will return immediately and never wait.
     *
     * @param obj Object to wait on.
     * @param timeoutMillis Timeout in milliseconds, or zero to wait forever.
     * @param endCondition End condition to test.
     * @return True if the condition is satisfied, false if time ran out.
     * @throws InterruptedException If thread is interrupted.
     */
    public static boolean waitWithCondition(final Object obj, final long timeoutMillis, final BooleanSupplier endCondition)
            throws InterruptedException {
        if (timeoutMillis == 0) {
            waitWithCondition(obj, endCondition);
            return true;
        }
        final Instant start = Instant.now();
        while (!endCondition.getAsBoolean()) {
            final long duration = Duration.between(start, Instant.now()).toMillis();
            if (duration >= timeoutMillis) {
                return false;
            }
            obj.wait(timeoutMillis - duration);
        }
        return true;
    }

    private GObjects() {
        // Hiding constructor.
    }
}
