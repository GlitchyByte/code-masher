// Copyright 2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.cache;

import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GExpiringCacheListTest {

    private void delay(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException e) {
            // No-op.
        }
    }

    @Test
    void canGet() {
        final List<String> cache = new GExpiringCacheList<>(1, ChronoUnit.MINUTES);
        cache.add("one");
        assertEquals("one", cache.get(0));
    }

    @Test
    void canRemove() {
        final List<String> cache = new GExpiringCacheList<>(1, ChronoUnit.MINUTES);
        cache.add("one");
        cache.remove(0);
        assertTrue(cache.isEmpty());
    }

    @Test
    void itemExpiresAfterTime() {
        final List<String> cache = new GExpiringCacheList<>(500, ChronoUnit.MILLIS);
        cache.add("one");
        assertEquals("one", cache.get(0));
        delay(600);
        assertTrue(cache.isEmpty());
    }

    @Test
    void itemExpiresAndLaterItemRemains() {
        final List<String> cache = new GExpiringCacheList<>(500, ChronoUnit.MILLIS);
        cache.add("one");
        delay(300);
        assertEquals("one", cache.get(0));
        cache.add("two");
        delay(300);
        assertEquals("two", cache.get(0));
    }
}
