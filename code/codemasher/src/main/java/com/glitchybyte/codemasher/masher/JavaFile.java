// Copyright 2020-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.codemasher.masher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds a Java class file contents. Separating imports and principal class code.
 */
public final class JavaFile {

    /**
     * Factory to get a JavaFile directly from a path.
     *
     * @param path Path of the Java file.
     * @return A fully parsed JavaFile.
     * @throws IOException If there is a problem reading the file.
     */
    public static JavaFile from(final Path path) throws IOException {
        final String name = path.getFileName().toString();
        final List<String> lines = Files.readAllLines(path);
        return new JavaFile(name).parse(lines);
    }

    /**
     * Filename.
     */
    public final String name;

    /**
     * Number of lines in original file.
     */
    private int originalLineCount;

    /**
     * Imports in the original file.
     */
    private List<String> imports;

    /**
     * Contents of original compilation unit, without imports.
     */
    private List<String> contents;

    /**
     * File type.
     */
    private JavaFileType type;

    /**
     * Creates a Java file contents holder.
     *
     * @param name Filename.
     */
    private JavaFile(final String name) {
        this.name = name;
    }

    /**
     * Returns the number of lines in the original file.
     *
     * @return The number of lines in the original file.
     */
    public int getOriginalLineCount() {
        return originalLineCount;
    }

    /**
     * Returns the imports in the original file.
     *
     * @return The imports in the original file.
     */
    public List<String> getImports() {
        return imports;
    }

    /**
     * Returns the contents of the original compilation unit, without imports.
     *
     * @return The contents of the original compilation unit, without imports.
     */
    public List<String> getContents() {
        return contents;
    }

    /**
     * Returns the compilation unit file type.
     *
     * @return The compilation unit file type.
     */
    public JavaFileType getType() {
        return type;
    }

    /**
     * Parses the given lines and extracts imports and contents.
     *
     * @param lines All lines in the file.
     * @return A fully parsed JavaFile object.
     */
    private JavaFile parse(final List<String> lines) {
        originalLineCount = lines.size();
        imports = parseImports(lines);
        contents = parseContents(lines);
        type = extractType(contents.get(0));
        return this;
    }

    private JavaFileType extractType(final String line) {
        final String trimmedLine = line.trim();
        if (trimmedLine.contains("class ") && trimmedLine.endsWith(" {")) {
            return JavaFileType.CLASS;
        }
        if (trimmedLine.contains("interface ") && trimmedLine.endsWith(" {")) {
            return JavaFileType.INTERFACE;
        }
        if (trimmedLine.contains("enum ") && trimmedLine.endsWith(" {")) {
            return JavaFileType.ENUM;
        }
        return null;
    }

    /**
     * Extracts only the imports from the Java file.
     *
     * @return The imports from the Java file.
     */
    private List<String> parseImports(final List<String> lines) {
        final List<String> imports = new ArrayList<>();
        for (final String line: lines) {
            if (extractType(line) != null) {
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
     * Extracts the compilation unit code.
     *
     * @return The compilation unit code.
     */
    private List<String> parseContents(final List<String> lines) {
        final List<String> contents = new ArrayList<>();
        boolean foundUnit = false;
        for (final String line: lines) {
            if (!foundUnit) {
                if (extractType(line) != null) {
                    foundUnit = true;
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
