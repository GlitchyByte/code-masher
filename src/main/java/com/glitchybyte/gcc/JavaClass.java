/*
 * Copyright 2020 Luis Mejia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.glitchybyte.gcc;

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
    private final List<String> lines;

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
     * Extracts only the imports from the Java file.
     *
     * @return The imports from the Java file.
     */
    public List<String> getImports() {
        final List<String> imports = new ArrayList<>();
        boolean isDone;
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
