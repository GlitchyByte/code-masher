// Copyright 2014-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Base64;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * String utilities.
 */
public final class GStrings {

    /**
     * System new line separator.
     */
    public static final String NEW_LINE = System.lineSeparator();

    /**
     * Spaces for indentation.
     */
    public static final String SPACE_TAB = "  ";

    /**
     * Number format for minimal human representation of double as String.
     */
    private static final Format numberFormat = DecimalFormat.getNumberInstance(Locale.US);

    /**
     * Indents the string. It will correctly indent multiple lines
     * as long as they have system line separators.
     *
     * @param str String to indent.
     * @return Indented string.
     */
    public static String indent(final String str) {
        return str.contains(NEW_LINE) ?
                SPACE_TAB + str.replaceAll(NEW_LINE, NEW_LINE + SPACE_TAB) :
                SPACE_TAB + str;
    }

    /**
     * Convenience replacement for {@code String.format}, but it already includes US locale.
     *
     * @param format A format string.
     * @param args Arguments for format.
     * @return Formatted string.
     */
    public static String format(final String format, final Object... args) {
        return String.format(Locale.US, format, args);
    }

    /**
     * Convenience double to String formatter with human minimal representation.
     *
     * @param value Double to format into a String.
     * @return Minimal String representation of the value.
     */
    public static String fromDouble(final double value) {
        return numberFormat.format(value);
    }

    /**
     * Convenience float to String formatter with human minimal representation.
     *
     * @param value Float to format into a String.
     * @return Minimal String representation of the value.
     */
    public static String fromFloat(final float value) {
        return numberFormat.format(value);
    }

    /**
     * Convenience int to String formatter with human minimal representation.
     *
     * @param value Int to format into a String.
     * @return Minimal String representation of the value.
     */
    public static String fromInt(final int value) {
        return format("%,d", value);
    }

    /**
     * Convenience long to String formatter with human minimal representation.
     *
     * @param value Long to format into a String.
     * @return Minimal String representation of the value.
     */
    public static String fromLong(final long value) {
        return format("%,d", value);
    }

    /**
     * Convenience Collection to String representation.
     *
     * @param collection Collection to represent as String.
     * @param delimiter Delimiter to use between elements.
     * @return A String representing the collection.
     * @param <T> Type of elements in the collection.
     */
    public static <T> String fromCollection(final Collection<T> collection, final String delimiter) {
        return collection.stream().map(T::toString).collect(Collectors.joining(delimiter));
    }

    /**
     * Convenience Collection to String representation with default comma delimiter.
     *
     * @param collection Collection to represent as String.
     * @return A String representing the collection.
     * @param <T> Type of elements in the collection.
     */
    public static <T> String fromCollection(final Collection<T> collection) {
        return fromCollection(collection, ", ");
    }

    /**
     * Convenience base64 encoder. Assumes UTF-8 charset.
     *
     * @param message Message to encode.
     * @return Encoded message.
     */
    public static String base64Encode(final String message) {
        return Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Convenience base64 decoder. Assumes UTF-8 charset.
     *
     * @param encoded Encoded message.
     * @return Decoded message.
     */
    public static String base64Decode(final String encoded) {
        return new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
    }

    private GStrings() {
        // Hiding constructor.
    }
}
