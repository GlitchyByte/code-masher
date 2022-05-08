// Copyright 2021 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.cache;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.*;

/**
 * Key-value pair cache with expiring entries.
 *
 * @param <K> Type of key.
 * @param <V> Type of value.
 */
public class GTimedCacheMap<K, V> implements Map<K, V> {

    private final Map<K, V> cacheMap;
    private final Map<K, Instant> timeMap;
    private final TemporalAmount timeToLive;

    /**
     * Constructs a cache with the given time-to-live for entries.
     *
     * @param timeToLive Time to live for a given entry.
     */
    public GTimedCacheMap(final TemporalAmount timeToLive) {
        cacheMap = new HashMap<>();
        timeMap = new LinkedHashMap<>();
        this.timeToLive = timeToLive;
    }

    /**
     * Constructs a cache with the given time-to-live for entries.
     *
     * @param amount The amount of TTL duration.
     * @param unit The unit TLL is measured in.
     */
    public GTimedCacheMap(final long amount, final TemporalUnit unit) {
        this(Duration.of(amount, unit));
    }

    private Instant getExpirationFromNow() {
        return Instant.now().plus(timeToLive);
    }

    private void removeExpired() {
        final Instant now = Instant.now();
        for (final var iterator = timeMap.entrySet().iterator(); iterator.hasNext();) {
            final var entry = iterator.next();
            if (entry.getValue().isAfter(now)) {
                break;
            }
            iterator.remove();
            cacheMap.remove(entry.getKey());
        }
    }

    @Override
    public int size() {
        removeExpired();
        return cacheMap.size();
    }

    @Override
    public boolean isEmpty() {
        removeExpired();
        return cacheMap.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        removeExpired();
        return cacheMap.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        removeExpired();
        return cacheMap.containsValue(value);
    }

    @Override
    public V get(final Object key) {
        removeExpired();
        return cacheMap.get(key);
    }

    @Override
    public V put(final K key, final V value) {
        timeMap.remove(key);
        timeMap.put(key, getExpirationFromNow());
        return cacheMap.put(key, value);
    }

    @Override
    public V remove(final Object key) {
        timeMap.remove(key);
        return cacheMap.remove(key);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        final Instant expiration = getExpirationFromNow();
        map.keySet().forEach(key -> {
            timeMap.remove(key);
            timeMap.put(key, expiration);
        });
        cacheMap.putAll(map);
    }

    @Override
    public void clear() {
        timeMap.clear();
        cacheMap.clear();
    }

    @Override
    public Set<K> keySet() {
        removeExpired();
        return cacheMap.keySet();
    }

    @Override
    public Collection<V> values() {
        removeExpired();
        return cacheMap.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        removeExpired();
        return cacheMap.entrySet();
    }
}
