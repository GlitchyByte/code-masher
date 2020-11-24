package com.glitchybyte.gcc;

import java.util.ArrayList;
import java.util.List;

public final class JavaClass {

    public final String name;
    public final List<String> lines;

    public JavaClass(final String name, final List<String> lines) {
        this.name = name;
        this.lines = lines;
    }

    public List<String> getImports() {
        final List<String> imports = new ArrayList<>();
        boolean isDone = false;
        for (final String line: lines) {
            final String trimmed = line.trim();
            isDone = trimmed.contains("class ");
            if (isDone) {
                break;
            }
            if (trimmed.startsWith("import ")) {
                imports.add(trimmed);
            }
        }
        return imports;
    }

    public List<String> getContents() {
        final List<String> contents = new ArrayList<>();
        boolean foundClass = false;
        for (final String line: lines) {
            if (!foundClass) {
                foundClass = line.contains("class ");
                contents.add(line);
                continue;
            }
            if (line.startsWith("}")) {
                contents.add(line);
                break;
            }
            contents.add(line);
        }
        return contents;
    }
}
