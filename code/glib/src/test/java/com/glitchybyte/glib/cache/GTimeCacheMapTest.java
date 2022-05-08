// Copyright 2021 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.cache;

import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GTimeCacheMapTest {

    private void delay(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (final InterruptedException e) {
            // No-op.
        }
    }

    @Test
    void canGet() {
        final Map<String, String> cache = new GTimedCacheMap<>(1, ChronoUnit.MINUTES);
        cache.put("one", "first");
        final String output = cache.get("one");
        assertEquals("first", output);
    }

    @Test
    void canRemove() {
        final Map<String, String> cache = new GTimedCacheMap<>(1, ChronoUnit.MINUTES);
        cache.put("one", "first");
        cache.remove("one");
        assertTrue(cache.isEmpty());
    }

    @Test
    void entryExpiresAfterTime() {
        final Map<String, String> cache = new GTimedCacheMap<>(500, ChronoUnit.MILLIS);
        cache.put("one", "first");
        delay(600);
        final String output = cache.get("one");
        assertNull(output);
    }

    @Test
    void entryExpirationResetsAfterReAdding() {
        final Map<String, String> cache = new GTimedCacheMap<>(500, ChronoUnit.MILLIS);
        cache.put("one", "first");
        delay(400);
        cache.put("one", "first");
        delay(200);
        final String output = cache.get("one");
        assertEquals("first", output);
    }

    @Test
    void entryExpiresAndLaterEntryRemains() {
        final Map<String, String> cache = new GTimedCacheMap<>(500, ChronoUnit.MILLIS);
        cache.put("one", "first");
        delay(300);
        assertEquals("first", cache.get("one"));
        cache.put("two", "second");
        delay(300);
        assertNull(cache.get("one"));
        assertEquals("second", cache.get("two"));
    }
}
