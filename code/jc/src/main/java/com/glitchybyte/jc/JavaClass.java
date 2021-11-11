// Copyright 2020-2021 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.jc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds a Java class file contents. Separating imports and principal class code.
 */
public final class JavaClass {

    /**
     * Filename.
     */
    public final String name;

    /**
     * All lines in the file.
     */
    public final List<String> lines;

    /**
     * Creates a Java file contents holder.
     *
     * @param name Filename.
     * @param lines All lines in the file.
     */
    public JavaClass(final String name, final List<String> lines) {
        this.name = name;
        this.lines = lines;
    }

    /**
     * Creates a Java file contents holder.
     *
     * @param classPath Path to file.
     * @throws IOException If there is a problem reading the file.
     */
    public JavaClass(final Path classPath) throws IOException {
        this(classPath.getFileName().toString(), Files.readAllLines(classPath));
    }

    /**
     * Extracts only the imports from the Java file.
     *
     * @return The imports from the Java file.
     */
    public List<String> getImports() {
        final List<String> imports = new ArrayList<>();
        for (final String line: lines) {
            if (line.contains("class ")) {
                break;
            }
            final String trimmed = line.trim();
            if (trimmed.startsWith("import ")) {
                imports.add(trimmed);
            }
        }
        return imports;
    }

    /**
     * Extracts the principal class code.
     *
     * @return The principal class code.
     */
    public List<String> getContents() {
        final List<String> contents = new ArrayList<>();
        boolean foundClass = false;
        for (final String line: lines) {
            if (!foundClass) {
                if (line.contains("class ")) {
                    foundClass = true;
                    contents.add(line);
                }
                continue;
            }
            contents.add(line);
            if (line.startsWith("}")) {
                break;
            }
        }
        return contents;
    }
}
