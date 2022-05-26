// Copyright 2020-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.codemasher;

import com.glitchybyte.glib.GStrings;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class Coalescer {

    /**
     * Magic comment to be replaced by a timestamp.
     */
    private static final String MAGIC_COMMENT_UPDATESTAMP = "[[CM::UPDATESTAMP]]";

    /**
     * Magic comment to be replaced by all other Java classes.
     */
    private static final String MAGIC_COMMENT_CODE = "[[CM::CODE]]";

    /**
     * Timestamp formatter.
     */
    private static final DateTimeFormatter timestampFormatter =
            DateTimeFormatter.ofPattern("uuuu-MM-dd|HH:mm:ss.SSS", Locale.US)
                    .withZone(ZoneOffset.systemDefault());

    /**
     * Timestamp of this instant.
     *
     * @return A string timestamp of this instant.
     */
    private static String getTimestamp() {
        return timestampFormatter.format(Instant.now());
    }

    private final List<JavaFile> javaFiles;
    private final String mainClassName;

    /**
     * Creates the coalescer.
     *
     * @param javaFiles Java classes to coalesce.
     * @param mainClassName Main class name.
     */
    public Coalescer(final List<JavaFile> javaFiles, final String mainClassName) {
        this.javaFiles = javaFiles;
        this.mainClassName = mainClassName;
    }

    public String coalesce() {
        final StringBuilder sb = new StringBuilder();
        writeImports(sb);
        final String mainClassFilename = mainClassName + ".java";
        JavaFile mainClass = null;
        final List<JavaFile> otherClasses = new ArrayList<>(javaFiles.size() - 1);
        for (final JavaFile javaFile: javaFiles) {
            if (javaFile.name.equals(mainClassFilename)) {
                mainClass = javaFile;
            } else {
                otherClasses.add(javaFile);
            }
        }
        if (mainClass == null) {
            throw new IllegalStateException("Main class not found!");
        }
        writeMainClass(sb, mainClass, otherClasses);
        return sb.toString();
    }

    private void writeImports(final StringBuilder sb) {
        final Set<String> imports = new HashSet<>();
        javaFiles.forEach(javaFile -> imports.addAll(javaFile.getImports()));
        for (final Iterator<String> iterator = imports.iterator(); iterator.hasNext();) {
            final String line = iterator.next();
            if (line.endsWith(".*;")) {
                continue;
            }
            final int dotPos = line.lastIndexOf('.');
            final String wildcardImport = line.substring(0, dotPos + 1) + "*;";
            if (imports.contains(wildcardImport)) {
                iterator.remove();
            }
        }
        for (final String line: imports) {
            sb.append(line).append(GStrings.NEW_LINE);
        }
        sb.append(GStrings.NEW_LINE);
    }

    private void writeMainClass(final StringBuilder sb, final JavaFile mainClass, final List<JavaFile> otherClasses) {
        for (final String line: mainClass.getContents()) {
            if (line.contains(MAGIC_COMMENT_UPDATESTAMP)) {
                sb.append("    // ").append(getTimestamp()).append(GStrings.NEW_LINE);
                final String[] graphic = new Graphic(40, 5, 10).toStringArray();
                for (final String graphicLine: graphic) {
                    sb.append("    // ").append(graphicLine).append(GStrings.NEW_LINE);
                }
                continue;
            } else if (line.contains(MAGIC_COMMENT_CODE)) {
                for (final JavaFile javaFile: otherClasses) {
                    writeOtherClass(sb, javaFile);
                    sb.append(GStrings.NEW_LINE);
                }
                continue;
            }
            sb.append(line).append(GStrings.NEW_LINE);
        }
    }

    private void writeOtherClass(final StringBuilder sb, final JavaFile javaFile) {
        boolean first = true;
        for (final String line: javaFile.getContents()) {
            if (first) {
                sb.append("    ");
                if (javaFile.getType() == JavaFileType.CLASS) {
                    sb.append("static ");
                }
                sb.append(line).append(GStrings.NEW_LINE);
                first = false;
                continue;
            }
            sb.append("    ").append(line).append(GStrings.NEW_LINE);
        }
    }
}
