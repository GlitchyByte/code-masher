/*
 * Copyright 2020 Luis Mejia
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
