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
}
