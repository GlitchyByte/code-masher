// Copyright 2014-2021 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.log;

import com.glitchybyte.glib.GObjects;
import com.glitchybyte.glib.GStrings;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.*;

/**
 * Logger class.
 * <p>
 * Implements a nice time format and shows thread name.
 * Made to log one-liners (except for exceptions).
 * Set logger name before use to change default name.
 * Default logging level is CONFIG.
 */
public final class GLog {

    private static String loggerName = "com.glitchybyte";

    private static final DateTimeFormatter localDateFormatter =
            DateTimeFormatter.ofPattern("uuuu-MM-dd|HH:mm:ss.SSS", Locale.US)
                    .withZone(ZoneOffset.systemDefault());

    private static Logger logger = null;

    private static Logger createLogger(final String loggerName) {
        final Logger logger = Logger.getLogger(loggerName);
        logger.setUseParentHandlers(false);
        final ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            @Override
            public String format(final LogRecord record) {
                final String dateTime = localDateFormatter.format(record.getInstant());
                final Object[] parameters = record.getParameters();
                final String threadName;
                final String className;
                final String message;
                final Throwable throwable;
                if ((parameters == null) || (parameters.length == 0)) {
                    threadName = null;
                    className = null;
                    message = record.getMessage();
                    throwable = record.getThrown();
                } else {
                    threadName = GObjects.castOrNull(parameters[0], String.class);
                    className = GObjects.castOrNull(parameters[1], String.class);
                    if (parameters.length > 2) {
                        throwable = GObjects.castOrNull(parameters[2], Throwable.class);
                        message = throwable != null ? throwable.toString() : record.getMessage();
                    } else {
                        message = record.getMessage();
                        throwable = null;
                    }
                }
                final StringBuilder sb = new StringBuilder(dateTime);
                sb.append(' ');
                sb.append(record.getLevel().getName());
                if (threadName != null) {
                    sb.append(" [");
                    sb.append(threadName);
                    sb.append(']');
                }
                if (className != null) {
                    sb.append(' ');
                    final int p = className.lastIndexOf('.');
                    sb.append(p == -1 ? className : className.substring(p + 1));
                    sb.append(':');
                }
                sb.append(' ');
                sb.append(message);
                sb.append(GStrings.NEW_LINE);
                if (throwable != null) {
                    for (final var element: throwable.getStackTrace()) {
                        sb.append(GStrings.SPACE_TAB);
                        sb.append(element.toString());
                        sb.append(GStrings.NEW_LINE);
                    }
                }
                return sb.toString();
            }
        });
        handler.setLevel(Level.ALL);
        logger.addHandler(handler);
        return logger;
    }

    private static Logger getLogger() {
        if (logger == null) {
            logger = createLogger(loggerName);
            logger.setLevel(Level.CONFIG);
        }
        return logger;
    }

    /**
     * Resets the default logger.
     * <p>
     * Clears the default logger. Next time a log is issued, a new logger is created.
     */
    public static void resetLogger() {
        logger = null;
    }

    private static final Map<String, Logger> extraLoggers = new HashMap<>();

    /**
     * Register a non-default logger.
     *
     * @param name Logger name.
     */
    public static void registerLogger(final String name) {
        extraLoggers.put(name, createLogger(name));
    }

    /**
     * Returns the names of non-default loggers.
     *
     * @return Names of non-default loggers.
     */
    public static Set<String> getRegisteredLoggers() {
        return extraLoggers.keySet();
    }

    /**
     * Sets the name of the logger for the whole application.
     *
     * @param name Logger name.
     */
    public static void setName(final String name) {
        if (logger != null) {
            throw new IllegalStateException("Logger already created. Name can't be set.");
        }
        loggerName = name;
    }

    /**
     * Sets the minimum level to log.
     *
     * @param level Minimum log level.
     */
    public static void setLevel(final Level level) {
        final Logger logger = getLogger();
        logger.setLevel(level);
    }

    /**
     * Logs a message at the specified level.
     *
     * @param level Log level for the message.
     * @param format Format of message to log.
     * @param args Arguments of format.
     */
    public static void log(final Level level, final String format, final Object... args) {
        final Logger logger = getLogger();
        final Thread currentThread = Thread.currentThread();
        logger.log(level, GStrings.format(format, args), new Object[] {
                currentThread.getName(),
                currentThread.getStackTrace()[3].getClassName()
        });
    }

    private static void log(final Level level, final Throwable throwable) {
        final Logger logger = getLogger();
        final Thread currentThread = Thread.currentThread();
        logger.log(level, null, new Object[] {
                currentThread.getName(),
                currentThread.getStackTrace()[3].getClassName(),
                throwable
        });
    }

    /**
     * Logs a message at {@code FINEST} level.
     *
     * @param format Format of message to log.
     * @param args Arguments of format.
     */
    public static void finest(final String format, final Object... args) {
        log(Level.FINEST, format, args);
    }

    /**
     * Logs a Throwable at {@code FINEST} level.
     *
     * @param throwable Throwable to log.
     */
    public static void finest(final Throwable throwable) {
        log(Level.FINEST, throwable);
    }

    /**
     * Logs a message at {@code FINER} level.
     *
     * @param format Format of message to log.
     * @param args Arguments of format.
     */
    public static void finer(final String format, final Object... args) {
        log(Level.FINER, format, args);
    }

    /**
     * Logs a Throwable at {@code FINER} level.
     *
     * @param throwable Throwable to log.
     */
    public static void finer(final Throwable throwable) {
        log(Level.FINER, throwable);
    }

    /**
     * Logs a message at {@code FINE} level.
     *
     * @param format Format of message to log.
     * @param args Arguments of format.
     */
    public static void fine(final String format, final Object... args) {
        log(Level.FINE, format, args);
    }

    /**
     * Logs a Throwable at {@code FINE} level.
     *
     * @param throwable Throwable to log.
     */
    public static void fine(final Throwable throwable) {
        log(Level.FINE, throwable);
    }

    /**
     * Logs a message at {@code CONFIG} level.
     *
     * @param format Format of message to log.
     * @param args Arguments of format.
     */
    public static void config(final String format, final Object... args) {
        log(Level.CONFIG, format, args);
    }

    /**
     * Logs a Throwable at {@code CONFIG} level.
     *
     * @param throwable Throwable to log.
     */
    public static void config(final Throwable throwable) {
        log(Level.CONFIG, throwable);
    }

    /**
     * Logs a message at {@code INFO} level.
     *
     * @param format Format of message to log.
     * @param args Arguments of format.
     */
    public static void info(final String format, final Object... args) {
        log(Level.INFO, format, args);
    }

    /**
     * Logs a Throwable at {@code INFO} level.
     *
     * @param throwable Throwable to log.
     */
    public static void info(final Throwable throwable) {
        log(Level.INFO, throwable);
    }

    /**
     * Logs a message at {@code WARNING} level.
     *
     * @param format Format of message to log.
     * @param args Arguments of format.
     */
    public static void warning(final String format, final Object... args) {
        log(Level.WARNING, format, args);
    }

    /**
     * Logs a Throwable at {@code WARNING} level.
     *
     * @param throwable Throwable to log.
     */
    public static void warning(final Throwable throwable) {
        log(Level.WARNING, throwable);
    }

    /**
     * Logs a message at {@code SEVERE} level.
     *
     * @param format Format of message to log.
     * @param args Arguments of format.
     */
    public static void severe(final String format, final Object... args) {
        log(Level.SEVERE, format, args);
    }

    /**
     * Logs a Throwable at {@code SEVERE} level.
     *
     * @param throwable Throwable to log.
     */
    public static void severe(final Throwable throwable) {
        log(Level.SEVERE, throwable);
    }

    private GLog() {
        // Hiding constructor.
    }
}
