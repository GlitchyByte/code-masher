// Copyright 2014-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.wrapped;

/**
 * Wrapper around a single object.
 *
 * <p>This wrapper can wrap any value and then be used in a lambda.
 *
 * @param <T> Type of value being held.
 */
public class GWrappedObject<T> {

    /**
     * Wrapped object.
     */
    public T value;

    /**
     * Constructs a wrapper.
     *
     * @param value Initial value.
     */
    public GWrappedObject(final T value) {
        this.value = value;
    }

    /**
     * Constructs a wrapper with a null object.
     */
    public GWrappedObject() {
        this(null);
    }

    /**
     * Tests if wrapper is holding a null.
     *
     * @return True if null.
     */
    public boolean isNull() {
        return value == null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{value=" + value + '}';
    }
}
