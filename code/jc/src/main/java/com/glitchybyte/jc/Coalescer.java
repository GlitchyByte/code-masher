// Copyright 2020-2021 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.jc;

import com.glitchybyte.glib.GStrings;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class Coalescer {

    /**
     * Magic comment to be replaced by a timestamp.
     */
    private static final String MAGIC_COMMENT_UPDATESTAMP = "[[JC::UPDATESTAMP]]";

    /**
     * Magic comment to be replaced by all other Java classes.
     */
    private static final String MAGIC_COMMENT_CODE = "[[JC::CODE]]";

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

    private final List<JavaClass> javaClasses;
    private final String mainClassName;

    /**
     * Creates the coalescer.
     *
     * @param javaClasses Java classes to coalesce.
     * @param mainClassName Main class name.
     */
    public Coalescer(final List<JavaClass> javaClasses, final String mainClassName) {
        this.javaClasses = javaClasses;
        this.mainClassName = mainClassName;
    }

    public String coalesce() {
        final StringBuilder sb = new StringBuilder();
        writeImports(sb);
        final String mainClassFilename = mainClassName + ".java";
        JavaClass mainClass = null;
        final List<JavaClass> otherClasses = new ArrayList<>(javaClasses.size() - 1);
        for (final JavaClass javaClass: javaClasses) {
            if (javaClass.name.equals(mainClassFilename)) {
                mainClass = javaClass;
            } else {
                otherClasses.add(javaClass);
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
        javaClasses.forEach(javaClass -> imports.addAll(javaClass.getImports()));
        for (final Iterator<String> iterator = imports.iterator(); iterator.hasNext() ;) {
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

    private void writeMainClass(final StringBuilder sb, final JavaClass mainClass, final List<JavaClass> otherClasses) {
        for (final String line: mainClass.getContents()) {
            if (line.contains(MAGIC_COMMENT_UPDATESTAMP)) {
                sb.append("    // ").append(getTimestamp()).append(GStrings.NEW_LINE);
                final String[] graphic = new Graphic(40, 5, 10).toStringArray();
                for (final String graphicLine: graphic) {
                    sb.append("    // ").append(graphicLine).append(GStrings.NEW_LINE);
                }
                continue;
            } else if (line.contains(MAGIC_COMMENT_CODE)) {
                for (final JavaClass javaClass: otherClasses) {
                    writeOtherClass(sb, javaClass);
                    sb.append(GStrings.NEW_LINE);
                }
                continue;
            }
            sb.append(line).append(GStrings.NEW_LINE);
        }
    }

    private void writeOtherClass(final StringBuilder sb, final JavaClass javaClass) {
        boolean first = true;
        for (final String line: javaClass.getContents()) {
            if (first) {
                sb.append("    static ").append(line).append(GStrings.NEW_LINE);
                first = false;
                continue;
            }
            sb.append("    ").append(line).append(GStrings.NEW_LINE);
        }
    }
}
