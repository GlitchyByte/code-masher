// Copyright 2021-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.codemasher;

import com.glitchybyte.codemasher.masher.JavaFile;
import com.glitchybyte.glib.GStrings;
import com.glitchybyte.glib.console.GConsole;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class MiniDisplay {

    private static final List<String> EMPTY_WATCHED_DIRECTORY =
            List.of(GConsole.coloredText("Nothing", GConsole.COLOR_BRIGHT_YELLOW));

    private int linesPrinted = 0;
    private String watchedDirectory = "";
    private List<String> javaFilesString = EMPTY_WATCHED_DIRECTORY;
    private String compilationString = "";

    public void clear() {
        GConsole.print(GConsole.cursorUp(linesPrinted));
        for (int i = 0; i < linesPrinted; ++i) {
            GConsole.println(GConsole.clearCurrentLine());
        }
        GConsole.print(GConsole.cursorUp(linesPrinted));
        linesPrinted = 0;
        GConsole.flush();
    }

    public void print() {
        clear();
        GConsole.println(watchedDirectory);
        GConsole.print("Files:" + " ".repeat(40 - 6));
        GConsole.println(javaFilesString.get(0));
        int index = 1;
        while (index < javaFilesString.size()) {
            GConsole.print(javaFilesString.get(index));
            ++index;
            if (((index % 2) != 0) || (index == javaFilesString.size())) {
                GConsole.println("");
            }
        }
        GConsole.println(compilationString);
        GConsole.println("Use %s to exit.", GConsole.coloredText("Ctrl + C", GConsole.COLOR_BRIGHT_WHITE));
        linesPrinted = 1 + // Watched dir.
                ((javaFilesString.size() + 1) / 2) + // Java files.
                1 + // Compilation.
                1; // Exit.
        GConsole.flush();
    }

    public void setWatchedDirectory(final Path watchedPath) {
        watchedDirectory = GStrings.format("Watching: %s", GConsole.coloredText(
                getTruncatedString(watchedPath.toString(), 80 - 10),
                GConsole.COLOR_BRIGHT_WHITE));
    }

    public void setInputFiles(final String mainJavaFilename, final List<JavaFile> javaFiles) {
        if (javaFiles.isEmpty()) {
            this.javaFilesString = EMPTY_WATCHED_DIRECTORY;
            return;
        }
        javaFilesString = new ArrayList<>(javaFiles.size());
        final JavaFile mainJavaFile = javaFiles.stream()
                .filter(javaFile -> javaFile.name.equals(mainJavaFilename))
                .findFirst()
                .orElseThrow();
        javaFilesString.add(getJavaFileString(mainJavaFile));
        javaFiles.stream()
                .filter(javaFile -> !javaFile.name.equals(mainJavaFilename))
                .sorted(Comparator.comparing(s -> s.name))
                .map(this::getJavaFileString)
                .forEach(javaFilesString::add);
    }

    private String getJavaFileString(final JavaFile javaFile) {
        final String lineCount = GStrings.fromInt(javaFile.getOriginalLineCount());
        final int availableLength = 40 - lineCount.length() - 3;
        final String name = getTruncatedString(javaFile.name, availableLength);
        return GStrings.format("%s (%s)%s",
                GConsole.coloredText(name, GConsole.COLOR_CYAN),
                GConsole.coloredText(lineCount, GConsole.COLOR_BRIGHT_BLACK),
                " ".repeat(availableLength - name.length())
        );
    }

    private String getTruncatedString(final String line, final int maxLength) {
        final int length = line.length();
        if (length <= maxLength) {
            return line;
        }
        final int removeCount = length + 3 - maxLength;
        final int cutIndex = (length - removeCount) / 2;
        return line.substring(0, cutIndex) + "..." + line.substring(cutIndex + removeCount);
    }

    public void setCompilationResult(final String classCode, final boolean success) {
        final long lineCount = classCode.lines().count();
        compilationString = GStrings.format("Compilation: %s (%s)",
                success ? GConsole.coloredText("SUCCESS", GConsole.COLOR_BRIGHT_GREEN) :
                        GConsole.coloredText("FAILURE", GConsole.COLOR_BRIGHT_RED),
                GConsole.coloredText(GStrings.fromLong(lineCount), GConsole.COLOR_BRIGHT_BLACK)
        );
    }
}
