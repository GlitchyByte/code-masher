// Copyright 2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.function;

/**
 * Represents a function that takes one argument and can throw an exception.
 *
 * @param <T> Type of argument of the function.
 * @param <R> Type of result of the function.
 */
@FunctionalInterface
public interface GFunctionWithException<T, R> {

    /**
     * Applies the function.
     *
     * @param arg Function argument.
     * @return A result.
     * @throws Exception Some exception.
     */
    R apply(final T arg) throws Exception;

    /**
     * Get value from function or default if there is an exception.
     *
     * @param function Function.
     * @param arg Argument for function.
     * @param defaultValue Default value if there is an exception.
     * @return Value from function or default if there is an exception.
     * @param <T> Type of argument.
     * @param <R> Type of return value.
     */
    static <T, R> R applyOrDefault(final GFunctionWithException<T, R> function, final T arg, final R defaultValue) {
        try {
            return function.apply(arg);
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    /**
     * Get value from function or null if there is an exception.
     *
     * @param function Function.
     * @param arg Argument for function.
     * @return Value from function or null if there is an exception.
     * @param <T> Type of argument.
     * @param <R> Type of return value.
     */
    static <T, R> R applyOrNull(final GFunctionWithException<T, R> function, final T arg) {
        return applyOrDefault(function, arg, null);
    }
}
