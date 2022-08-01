// Copyright 2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.process;

import com.glitchybyte.glib.GObjects;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An encapsulation that represents and manages a system process.
 */
public final class GProcess implements Callable<Integer> {

    /**
     * Process state.
     */
    public enum State {
        /**
         * Process created.
         */
        CREATED,

        /**
         * Process started.
         */
        STARTED,

        /**
         * Process stopping.
         */
        STOPPING,

        /**
         * Process stopped.
         */
        STOPPED,

        /**
         * Process canceled.
         */
        CANCELED
    }

    private final String[] command;
    private final Path dir;
    private final int maxOutputBufferLines;
    private final boolean autoPrintOutput;
    private State state = State.CREATED;
    private ExecutorService outputCollectorExecutor;
    private Process process;
    private GProcessOutputCollector outputCollector;

    /**
     * Creates a process from the given command and starting directory.
     *
     * @param command Array representing the parts of the command.
     * @param dir Starting directory. If null, it will inherit the current process current directory.
     * @param maxOutputBufferLines Max lines to keep in memory from console output of this process.
     */
    public GProcess(final String[] command, final Path dir, final int maxOutputBufferLines) {
        this.command = command;
        this.dir = dir;
        this.maxOutputBufferLines = maxOutputBufferLines;
        this.autoPrintOutput = false;
    }

    /**
     * Creates a process from the given command and starting directory.
     *
     * @param command Array representing the parts of the command.
     * @param dir Starting directory. If null, it will inherit the current process current directory.
     * @param autoPrintOutput This convenience parameter makes it so output of the process gets continually printed to
     *                        the current process' console.
     */
    public GProcess(final String[] command, final Path dir, final boolean autoPrintOutput) {
        this.command = command;
        this.dir = dir;
        this.maxOutputBufferLines = autoPrintOutput ? 1_000 : 0;
        this.autoPrintOutput = autoPrintOutput;
    }

    /**
     * Convenience constructor to quickly execute a process without any concern for its output.
     *
     * @param command Array representing the parts of the command.
     * @param dir Starting directory. If null, it will inherit the current process current directory.
     */
    public GProcess(final String[] command, final Path dir) {
        this(command, dir, false);
    }

    /**
     * Convenience constructor to quickly execute a process without any concern for its output. It will also
     * default to the current process' current directory.
     *
     * @param command Array representing the parts of the command.
     */
    public GProcess(final String[] command) {
        this(command, null);
    }

    /**
     * Returns the process handle to query for other information about the process.
     *
     * @return The process handle to query for other information about the process.
     */
    public synchronized ProcessHandle getProcessHandle() {
        return process.toHandle();
    }

    /**
     * Spawns and starts the process, waiting for its termination on a separate thread.
     *
     * @return The process exit code.
     */
    @Override
    public Integer call() {
        synchronized (this) {
            final ProcessBuilder pb = GOSInterface.instance.createProcessBuilder(command, dir);
            try {
                process = pb.start();
            } catch (final IOException e) {
                process = null;
                outputCollector = null;
                return null;
            }
            outputCollectorExecutor = Executors.newSingleThreadExecutor();
            outputCollector = new GProcessOutputCollector(process, maxOutputBufferLines);
            outputCollectorExecutor.submit(outputCollector);
            setState(State.STARTED);
        }
        try {
            if (autoPrintOutput) {
                while ((state == State.STARTED) && process.isAlive()) {
                    GObjects.hold(this, 1_000);
                    printOutput();
                }
                final int statusCode = process.waitFor();
                printOutput();
                setState(State.STOPPED);
                return statusCode;
            } else {
                final int statusCode = process.waitFor();
                setState(State.STOPPED);
                return statusCode;
            }
        } catch (final InterruptedException e) {
            setState(State.CANCELED);
            return null;
        }
    }

    /**
     * Stops this process or any subprocess of this process.
     *
     * <p>The implementation will send a SIGINT to the given pid.
     *
     * <p>*IMPORTANT!* This is a convenience for when you need to inform a subprocess you want to exit for orderly
     * shutdown up the chain. For example, an application started by a Windows batch script will not shut down if you
     * send a SIGINT to the batch process.
     *
     * @param pid Pid of the descendant process to stop.
     */
    public synchronized void stop(final long pid) {
        if (state != State.STARTED) {
            return;
        }
        final boolean isDescendant = process.descendants().anyMatch(processHandle -> pid == processHandle.pid());
        if (!isDescendant) {
            throw new IllegalArgumentException("pid is not a descendant of this process.");
        }
        GOSInterface.instance.sendSignalINT(pid);
        outputCollectorExecutor.shutdown();
        setState(State.STOPPING);
    }

    /**
     * Stops the process.
     *
     * <p>The implementation will send a SIGINT.
     */
    public synchronized void stop() {
        stop(process.pid());
    }

    /**
     * Gets the process state.
     *
     * @return The process state.
     */
    public synchronized State getState() {
        return state;
    }

    private synchronized void setState(final State state) {
        this.state = state;
        notifyAll();
    }

    /**
     * Convenience method to wait until a given state is reached.
     *
     * @param state Wanted state.
     * @param timeoutMillis Time to wait for state.
     * @return True if the state has been reached.
     * @throws InterruptedException If the wait was interrupted.
     */
    public synchronized boolean waitForState(final State state, final long timeoutMillis) throws InterruptedException {
        GObjects.waitWithCondition(this, timeoutMillis, () -> this.state == state);
        return this.state == state;
    }

    /**
     * Convenience method to wait indefinitely until a given state is reached.
     *
     * @param state Wanted state.
     * @return True if the state has been reached.
     * @throws InterruptedException If the wait was interrupted.
     */
    public synchronized boolean waitForState(final State state) throws InterruptedException {
        return waitForState(state, 0);
    }

    /**
     * Returns all collected output until this moment. Resets collection buffer.
     *
     * @return All collected lines until this moment.
     */
    public List<String> getOutput() {
        return outputCollector == null ? Collections.emptyList() : outputCollector.getOutput();
    }

    private void printOutput() {
        outputCollector.getOutput().forEach(System.out::println);
    }
}
