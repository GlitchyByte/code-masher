// Copyright 2020 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.log;

import com.glitchybyte.glib.GStrings;

import java.time.Duration;
import java.time.Instant;

/**
 * Timer class for on-the-fly quick profiling.
 * This DOES NOT replace a real profiler.
 */
public final class GTimer {

    private final Instant startTime = Instant.now();

    /**
     * Logs current elapsed time and a message.
     *
     * @param format Format of message to log.
     * @param args Arguments of format.
     */
    public void print(final String format, final Object... args) {
        final double elapsed = (double) Duration.between(startTime, Instant.now()).toNanos() / 1_000_000;
        final String timer = GStrings.format("{%.3f} ", elapsed);
        GLog.info(timer + format, args);
    }
}
