// Copyright 2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Console output collector for capturing an external process output.
 */
public final class GProcessOutputCollector implements Runnable {

    private final Process process;
    private final int maxOutputBufferLines;
    private List<String> outputLines;

    /**
     * Creates an output collector.
     *
     * @param process Process to which attach this collector.
     * @param maxOutputBufferLines Max lines to hold in memory. If output exceeds this quantity before being queried,
     *                             older lines will be lost. A value of zero will prevent any collection.
     */
    public GProcessOutputCollector(final Process process, final int maxOutputBufferLines) {
        this.process = process;
        this.maxOutputBufferLines = maxOutputBufferLines;
        outputLines = maxOutputBufferLines == 0 ? null : createOutputBuffer();
    }

    private List<String> createOutputBuffer() {
        return new LinkedList<>();
    }

    /**
     * Collector runner. This must be called on a different thread.
     */
    @Override
    public void run() {
        try (final BufferedReader reader = process.inputReader(StandardCharsets.UTF_8)) {
            String line;
            do {
                line = reader.readLine();
                if (line != null) {
                    addLineToOutput(line);
                }
            } while (line != null);
        } catch (final IOException e) {
            // No-op.
        }
    }

    private void addLineToOutput(final String line) {
        if (maxOutputBufferLines == 0) {
            return;
        }
        synchronized (this) {
            while (outputLines.size() >= maxOutputBufferLines) {
                outputLines.remove(0);
            }
            outputLines.add(line);
        }
    }

    /**
     * Returns all collected output until this moment. Resets collection buffer.
     *
     * @return All collected lines until this moment.
     */
    public synchronized List<String> getOutput() {
        if (maxOutputBufferLines == 0) {
            return Collections.emptyList();
        }
        final List<String> output = outputLines;
        outputLines = createOutputBuffer();
        return output;
    }
}
