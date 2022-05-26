// Copyright 2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.cache;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.AbstractSequentialList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * List with expiring items.
 *
 * @param <T> Type of item.
 */
public class GExpiringCacheList<T> extends AbstractSequentialList<T> {

    private static final class CacheItem<T> {

        public final Instant expiration;
        public final T item;

        public CacheItem(final Instant expiration, final T item) {
            this.expiration = expiration;
            this.item = item;
        }
    }

    private final List<CacheItem<T>> cacheList;
    private final TemporalAmount timeToLive;

    /**
     * Creates a cache with the given time-to-live.
     *
     * @param timeToLive Time to live for a given item.
     */
    public GExpiringCacheList(final TemporalAmount timeToLive) {
        cacheList = new LinkedList<>();
        this.timeToLive = timeToLive;
    }

    /**
     * Creates a cache with the given time-to-live.
     *
     * @param amount The amount of TTL duration.
     * @param unit The unit TLL is measured in.
     */
    public GExpiringCacheList(final long amount, final TemporalUnit unit) {
        this(Duration.of(amount, unit));
    }

    private Instant getExpirationFromNow() {
        return Instant.now().plus(timeToLive);
    }

    private void removeExpired() {
        final Instant now = Instant.now();
        for (final var iterator = cacheList.iterator(); iterator.hasNext();) {
            final CacheItem<T> cacheItem = iterator.next();
            if (cacheItem.expiration.isAfter(now)) {
                break;
            }
            iterator.remove();
        }
    }

    @Override
    public int size() {
        removeExpired();
        return cacheList.size();
    }

    private final class CacheListIterator implements ListIterator<T> {

        private final ListIterator<CacheItem<T>> cacheListIterator;

        public CacheListIterator(final int index) {
            cacheListIterator = cacheList.listIterator(index);
        }

        @Override
        public boolean hasNext() {
            return cacheListIterator.hasNext();
        }

        @Override
        public T next() {
            return cacheListIterator.next().item;
        }

        @Override
        public boolean hasPrevious() {
            return cacheListIterator.hasPrevious();
        }

        @Override
        public T previous() {
            return cacheListIterator.previous().item;
        }

        @Override
        public int nextIndex() {
            return cacheListIterator.nextIndex();
        }

        @Override
        public int previousIndex() {
            return cacheListIterator.previousIndex();
        }

        @Override
        public void remove() {
            cacheListIterator.remove();
        }

        @Override
        public void set(final T item) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(final T item) {
            cacheListIterator.add(new CacheItem<>(getExpirationFromNow(), item));
        }
    }

    @Override
    public ListIterator<T> listIterator(final int index) {
        removeExpired();
        return new CacheListIterator(index);
    }
}
