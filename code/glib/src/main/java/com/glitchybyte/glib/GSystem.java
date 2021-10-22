// Copyright 2020 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib;

import sun.misc.Signal;

/**
 * General system helpers.
 */
public final class GSystem {

    /**
     * Waits for {@code SIGINT} (Ctrl+C).
     * <p>
     * This is useful for apps that never end (like services),
     * but you want to provide a nice break and exit anyway.
     *
     * @throws InterruptedException If thread is interrupted.
     */
    public static void waitForSigInt() throws InterruptedException {
        final Object monitor = new Object();
        Signal.handle(new Signal("INT"), signal -> {
            synchronized (monitor) {
                monitor.notifyAll();
            }
        });
        synchronized (monitor) {
            monitor.wait();
        }
    }

    private GSystem() {
        // Hiding constructor.
    }
}
