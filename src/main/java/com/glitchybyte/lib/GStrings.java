/*
 * Copyright 2014-2020 Luis Mejia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glitchybyte.lib;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Locale;

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
     * Convenience replacement for String.format, but it already includes US locale.
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

    private GStrings() {
        // Hiding constructor.
    }
}
