// Copyright 2020-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib;

import sun.misc.Signal;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Utility to monitor SIGINT and SIGTERM for proper application shutdown.
 */
public final class GShutdownMonitor {

    private static final AtomicBoolean shutdownLock = new AtomicBoolean(false);
    private static final Collection<GShutdownMonitor> shutdownMonitors = new HashSet<>();

    /**
     * Creates a monitor that will get notified when it's time for an orderly shutdown.
     *
     * @return A shutdown monitor.
     */
    public static GShutdownMonitor createShutdownMonitor() {
        synchronized (shutdownLock) {
            final boolean isShuttingDown = shutdownLock.get();
            final GShutdownMonitor monitor = new GShutdownMonitor(isShuttingDown);
            if (!isShuttingDown) {
                shutdownMonitors.add(monitor);
            }
            return monitor;
        }
    }

    private static void triggerShutdown(final Signal signal) {
        if (shutdownLock.get()) {
            return;
        }
        synchronized (shutdownLock) {
            shutdownLock.set(true);
            shutdownMonitors.forEach(GShutdownMonitor::shutdown);
            shutdownMonitors.clear();
        }
    }

    static {
        Signal.handle(new Signal("TERM"), GShutdownMonitor::triggerShutdown);
        Signal.handle(new Signal("INT"), GShutdownMonitor::triggerShutdown);
    }

    private boolean isShuttingDown;

    private GShutdownMonitor(final boolean isShuttingDown) {
        this.isShuttingDown = isShuttingDown;
    }

    /**
     * Returns true when an orderly shutdown should occur.
     *
     * @return True when an orderly shutdown should occur.
     */
    public synchronized boolean shouldShutdown() {
        return isShuttingDown;
    }

    /**
     * Manually triggers an orderly shutdown.
     */
    public synchronized void shutdown() {
        isShuttingDown = true;
        notifyAll();
    }

    /**
     * Holds execution of this thread until a shutdown is triggered or for the given milliseconds,
     * whichever happens first.
     *
     * <p>If a shutdown has been already triggered, the thread will not be held.
     *
     * @param timeoutMillis Timeout in milliseconds, or zero to wait forever.
     */
    public synchronized void hold(final long timeoutMillis) {
        if (shouldShutdown()) {
            return;
        }
        try {
            GObjects.hold(this, timeoutMillis);
        } catch (final InterruptedException e) {
            shutdown();
        }
    }

    /**
     * Holds execution of this thread until a shutdown is triggered.
     *
     * <p>If a shutdown has been already triggered, the thread will not be held.
     */
    public synchronized void hold() {
        hold(0);
    }

    /**
     * Convenience method to execute an action periodically at the given cadence,
     * until a shutdown is triggered.
     *
     * @param cadenceMillis Cadence at which to execute the action.
     * @param action Action to execute.
     */
    public void whileLive(final long cadenceMillis, final Runnable action) {
        while (!shouldShutdown()) {
            action.run();
            hold(cadenceMillis);
        }
    }
}
