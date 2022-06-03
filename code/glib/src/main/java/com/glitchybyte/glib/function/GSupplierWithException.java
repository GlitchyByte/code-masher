// Copyright 2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.function;

/**
 * Represents a supplier of results that can throw an exception.
 *
 * @param <T> Type of result supplied by this supplier.
 */
@FunctionalInterface
public interface GSupplierWithException<T> {

    /**
     * Gets a result.
     *
     * @return A result.
     * @throws Exception Some exception.
     */
    T get() throws Exception;

    /**
     * Get value from supplier or default if there is an exception.
     *
     * @param supplier Value supplier.
     * @param defaultValue Default value if there is an exception.
     * @return Value from supplier or default if there is an exception.
     * @param <T> Type of value.
     */
    static <T> T getOrDefault(final GSupplierWithException<T> supplier, final T defaultValue) {
        try {
            return supplier.get();
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    /**
     * Get value from supplier or null if there is an exception.
     *
     * @param supplier Value supplier.
     * @return Value from supplier or null if there is an exception.
     * @param <T> Type of value.
     */
    static <T> T getOrNull(final GSupplierWithException<T> supplier) {
        return getOrDefault(supplier, null);
    }
}
