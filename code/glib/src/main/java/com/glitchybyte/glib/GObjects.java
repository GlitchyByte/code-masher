// Copyright 2014-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib;

import com.glitchybyte.glib.function.GSupplierWithException;

import java.util.function.BooleanSupplier;

/**
 * Object utilities.
 */
public final class GObjects {

    /**
     * Waits on object until {@code endCondition} returns true.
     * <p>
     * Test happens before waiting, so if condition is true it will return immediately and never wait.
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
     * Returns the supplier result, or null if there was an exception.
     *
     * @param supplier Result supplier.
     * @return The supplier result, or null if there was an exception.
     * @param <T> Type of result.
     */
    public static <T> T suppliedObjectOrNull(final GSupplierWithException<T> supplier) {
        try {
            return supplier.get();
        } catch (final Exception e) {
            return null;
        }
    }

    private GObjects() {
        // Hiding constructor.
    }
}
