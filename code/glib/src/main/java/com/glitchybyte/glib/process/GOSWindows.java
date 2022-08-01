// Copyright 2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.glib.process;

import sun.misc.Signal;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiFunction;

/**
 * Specialization of GOSInterface for Windows.
 */
public final class GOSWindows extends GOSInterface {

    /**
     * Creates a Windows OS interface.
     */
    public GOSWindows() {
        super(GOSType.WINDOWS);
    }

    @Override
    public Path resolvedDir(final String dir) {
        if (dir.equals("~")) {
            return Paths.get(System.getProperty("user.home")).resolve(dir.substring(1)).normalize();
        }
        if (dir.startsWith("~/") || dir.startsWith("~\\")) {
            return Paths.get(System.getProperty("user.home")).resolve(dir.substring(2)).normalize();
        }
        return Paths.get(System.getProperty("user.dir")).resolve(dir).normalize();
    }

    @Override
    public String[] getShellCommand(final String command) {
        return new String[] {
                "cmd.exe",
                "/c",
                command
        };
    }

    @Override
    public String[] getSignalCommand(final Signal signal, final long pid) {
        if (hackGetSignalCommand == null) {
            throw new UnsupportedOperationException();
        }
        return hackGetSignalCommand.apply(signal, pid);
    }

    /**
     * Hack to provide a signal sender.
     *
     * <p><a href="https://github.com/ElyDotDev/windows-kill">windows-kill</a> works perfectly for this.
     *
     * <p>Once placed somewhere in your system it can be specified like this:
     * <pre>
     * GOSWindows.hackGetSignalCommand = (signal, pid) -> new String[] { "path/to/windows-kill.exe", "-SIG" + signal.getName(), pid.toString() };
     * </pre>
     */
    public static BiFunction<Signal, Long, String[]> hackGetSignalCommand = null;
}
