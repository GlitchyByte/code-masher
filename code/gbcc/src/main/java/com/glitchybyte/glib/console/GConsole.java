// Copyright 2020 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.console;

import com.glitchybyte.glib.GStrings;

import java.io.Console;
import java.io.PrintWriter;

/**
 * Helper class to print color text to console.
 * It assumes 256 colors (at least).
 * If there is no console, it will do nothing.
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * GConsole.print("This is " + GConsole.coloredText("cyan", GConsole.COLOR_CYAN, null) + " text.\n");
 * GConsole.println("And this is %s text.", GConsole.coloredText("orange", GConsole.rgb(5, 2, 1), null));
 * GConsole.flush();
 * }
 * </pre>
 * @see <a href="https://en.wikipedia.org/wiki/ANSI_escape_code#CSI_sequences">CSI sequences</a>
 */
public final class GConsole {

    private static final Console console = System.console();
    private static final PrintWriter writer = (console == null) ? null : console.writer();

    /*
     * Control Sequence Introducer constants.
     */
    private static final String CSI = "\u001b[";
//    private static final String CSI_CLEAR = CSI + "2J";
//    private static final String CSI_MOVE_TO = CSI + "%d;%df";
    private static final String CSI_CURSOR_UP = CSI + "%dA";
    private static final String CSI_CURSOR_DOWN = CSI + "%dB";
    private static final String CSI_CURSOR_FORWARD = CSI + "%dC";
    private static final String CSI_CURSOR_BACK = CSI + "%dD";
    private static final String CSI_COLOR_RESET = CSI + "0m";
//    private static final String CSI_COLOR_FOREGROUND = CSI + "3%dm";
//    private static final String CSI_COLOR_BACKGROUND = CSI + "4%dm";
//    private static final String CSI_COLOR_BRIGHT_ON = CSI + "1m";
//    private static final String CSI_COLOR_BRIGHT_OFF = CSI + "22m";
//    private static final String CSI_COLOR_INVERSE_ON = CSI + "2m";// "7m";
//    private static final String CSI_COLOR_INVERSE_OFF = CSI + "27m";
    private static final String CSI_COLOR_FOREGROUND = CSI + "38;5;%dm";
    private static final String CSI_COLOR_BACKGROUND = CSI + "48;5;%dm";

    private static final int COLORED_TEXT_MIN_BUFFER_LENGTH = (2 * 3) + // 2 codes of 3 characters each.
            CSI_COLOR_FOREGROUND.length() + CSI_COLOR_BACKGROUND.length() + CSI_COLOR_RESET.length();

    /**
     * Black.
     */
    public static final Integer COLOR_BLACK = 0;

    /**
     * Red.
     */
    public static final Integer COLOR_RED = 1;

    /**
     * Green.
     */
    public static final Integer COLOR_GREEN = 2;

    /**
     * Yellow.
     */
    public static final Integer COLOR_YELLOW = 3;

    /**
     * Blue.
     */
    public static final Integer COLOR_BLUE = 4;

    /**
     * Magenta.
     */
    public static final Integer COLOR_MAGENTA = 5;

    /**
     * Cyan.
     */
    public static final Integer COLOR_CYAN = 6;

    /**
     * White (light gray).
     */
    public static final Integer COLOR_WHITE = 7;

    /**
     * Bright black (dark gray).
     */
    public static final Integer COLOR_BRIGHT_BLACK = 8;

    /**
     * Bright red.
     */
    public static final Integer COLOR_BRIGHT_RED = 9;

    /**
     * Bright green.
     */
    public static final Integer COLOR_BRIGHT_GREEN = 10;

    /**
     * Bright yellow.
     */
    public static final Integer COLOR_BRIGHT_YELLOW = 11;

    /**
     * Bright blue.
     */
    public static final Integer COLOR_BRIGHT_BLUE = 12;

    /**
     * Bright magenta.
     */
    public static final Integer COLOR_BRIGHT_MAGENTA = 13;

    /**
     * Bright cyan.
     */
    public static final Integer COLOR_BRIGHT_CYAN = 14;

    /**
     * Bright white (white).
     */
    public static final Integer COLOR_BRIGHT_WHITE = 15;

    /**
     * Prints to console.
     *
     * @param format Format of message to print.
     * @param args Arguments of format.
     */
    public static void print(final String format, final Object... args) {
        if (writer == null) {
            return;
        }
        writer.write(GStrings.format(format, args));
    }

    /**
     * Prints to console, and adds a new line.
     *
     * @param format Format of message to print.
     * @param args Arguments of format.
     */
    public static void println(final String format, final Object... args) {
        print(format + GStrings.NEW_LINE, args);
    }

//    public static String clear() {
//        return CSI_CLEAR;
//    }

//    public static void moveTo(final int x, final int y) {
//        return GStrings.format(CSI_MOVE_TO, y + 1, x + 1);
//    }

    /**
     * Returns a {@code String} that will move the cursor up {@code n} number of times when printed.
     *
     * @param n Number of times to move the cursor.
     * @return {@code String} with encoded command.
     */
    public static String cursorUp(final int n) {
        return n == 0 ? "" : GStrings.format(CSI_CURSOR_UP, n);
    }

    /**
     * Returns a {@code String} that will move the cursor down {@code n} number of times when printed.
     *
     * @param n Number of times to move the cursor.
     * @return {@code String} with encoded command.
     */
    public static String cursorDown(final int n) {
        return n == 0 ? "" : GStrings.format(CSI_CURSOR_DOWN, n);
    }

    /**
     * Returns a {@code String} that will move the cursor left {@code n} number of times when printed.
     *
     * @param n Number of times to move the cursor.
     * @return {@code String} with encoded command.
     */
    public static String cursorLeft(final int n) {
        return n == 0 ? "" : GStrings.format(CSI_CURSOR_BACK, n);
    }

    /**
     * Returns a {@code String} that will move the cursor right {@code n} number of times when printed.
     *
     * @param n Number of times to move the cursor.
     * @return {@code String} with encoded command.
     */
    public static String cursorRight(final int n) {
        return n == 0 ? "" : GStrings.format(CSI_CURSOR_FORWARD, n);
    }

    /**
     * Returns a {@code String} that will resets colors when printed.
     *
     * @return {@code String} with encoded command.
     */
    public static String resetColor() {
        return CSI_COLOR_RESET;
    }

    /**
     * Creates the corresponding color code for the given RGB value.
     *
     * @param r Red component [0, 6)
     * @param g Green component [0, 6)
     * @param b Blue component [0, 6)
     * @return Color code.
     */
    public static Integer rgb(final int r, final int g, final int b) {
        if ((r >= 6) || (g >= 6) || (b >= 6)) {
            throw new IllegalArgumentException(GStrings.format("Bad RGB value. Max is 5. [%d, %d, %d]", r, g, b));
        }
        return 16 + (36 * r) + (6 * g) + b;
    }

    /**
     * Creates the corresponding color code for the given grey {@code step} value, from dark to bright.
     *
     * @param step Grey step [0, 24)
     * @return Color code.
     */
    public static Integer grey(final int step) {
        if (step >= 24) {
            throw new IllegalArgumentException(GStrings.format("Bad step value. Max is 23. [%d]", step));
        }
        return 232 + step;
    }

    /**
     * Returns a {@code String} that represents {@code text} with the given colors when printed.
     *
     * @param text Test to print.
     * @param fgColor Foreground color code.
     * @param bgColor Background color code.
     * @return {@code String} with encoded command.
     */
    public static String coloredText(final String text, final Integer fgColor, final Integer bgColor) {
        final StringBuilder sb = new StringBuilder(COLORED_TEXT_MIN_BUFFER_LENGTH + text.length());
        if (fgColor != null) {
            sb.append(GStrings.format(CSI_COLOR_FOREGROUND, fgColor));
        }
        if (bgColor != null) {
            sb.append(GStrings.format(CSI_COLOR_BACKGROUND, bgColor));
        }
        sb.append(text);
        sb.append(CSI_COLOR_RESET);
        return sb.toString();
    }

    /**
     * Returns a {@code String} that represents {@code text} with the given foreground color when printed.
     *
     * @param text Test to print.
     * @param fgColor Foreground color code.
     * @return {@code String} with encoded command.
     */
    public static String coloredText(final String text, final Integer fgColor) {
        return coloredText(text, fgColor, null);
    }

    /**
     * Flushes all printed commands.
     * <p>
     * It is possible console will not update until it's flushed.
     * Flush when you want to make sure the user sees your print output.
     */
    public static void flush() {
        if (writer == null) {
            return;
        }
        writer.flush();
    }

    private GConsole() {
        // Hiding constructor.
    }
}
