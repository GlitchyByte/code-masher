// Copyright 2021 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.jc;

import com.glitchybyte.glib.console.GConsole;

import java.util.List;

public final class MiniConsole {

    private int linesPrinted = 0;

    public void clear() {
        GConsole.print(GConsole.cursorUp(linesPrinted));
        for (int i = 0; i < linesPrinted; ++i) {
            GConsole.println(GConsole.clearCurrentLine());
        }
        GConsole.print(GConsole.cursorUp(linesPrinted));
        linesPrinted = 0;
        GConsole.flush();
    }

    public void printInputFiles(final List<JavaClass> javaClasses) {
        if (javaClasses.isEmpty()) {
            GConsole.println("Read: %s", GConsole.coloredText("Nothing", GConsole.COLOR_BRIGHT_YELLOW));
            linesPrinted += 1;
        } else {
            for (final JavaClass javaClass: javaClasses) {
                GConsole.println("Read: %s (%d lines)",
                        GConsole.coloredText(javaClass.name, GConsole.COLOR_BRIGHT_GREEN),
                        javaClass.lines.size());
            }
            linesPrinted += javaClasses.size();
        }
        GConsole.flush();
    }

    public void printOutputFile(final String filename, final String classCode) {
        if (classCode == null) {
            GConsole.println("Write: %s", GConsole.coloredText("Compilation error!", GConsole.COLOR_BRIGHT_RED));
        } else {
            final int lineCount = (int) classCode.lines().count();
            GConsole.println("Write: %s (%d lines)",
                    GConsole.coloredText(filename, GConsole.COLOR_BRIGHT_CYAN),
                    lineCount);
        }
        linesPrinted += 1;
        GConsole.flush();
    }
}
