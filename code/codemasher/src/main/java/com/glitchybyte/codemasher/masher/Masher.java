// Copyright 2021-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.codemasher.masher;

import com.glitchybyte.codemasher.MiniDisplay;
import com.glitchybyte.glib.function.GSupplierWithException;
import com.glitchybyte.glib.log.GLog;
import com.glitchybyte.glib.wrapped.GWrappedString;
import com.sun.nio.file.SensitivityWatchEventModifier;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class Masher implements Runnable {

    private final Path watchedPath;
    private final String mainJavaFilename;
    private final GWrappedString coalescedClass;
    private final MiniDisplay display;

    public Masher(final Path watchedPath, final String mainJavaFilename, final GWrappedString coalescedClass,
            final MiniDisplay miniDisplay) {
        this.watchedPath = watchedPath;
        this.mainJavaFilename = mainJavaFilename;
        this.coalescedClass = coalescedClass;
        display = miniDisplay;
        display.setWatchedDirectory(watchedPath);
    }

    @Override
    public void run() {
        watchSources(this::onSourceChange);
    }

    private void watchSources(final Supplier<Exception> onChange) {
        try {
            // Register watcher.
            final WatchService watchService = FileSystems.getDefault().newWatchService();
            watchedPath.register(watchService,
                    new WatchEvent.Kind[] {
                            StandardWatchEventKinds.ENTRY_CREATE,
                            StandardWatchEventKinds.ENTRY_DELETE,
                            StandardWatchEventKinds.ENTRY_MODIFY
                    },
                    SensitivityWatchEventModifier.HIGH
            );
            // Watch loop.
            while (true) {
                final Exception result = onChange.get();
                if (result != null) {
                    GLog.severe(result);
                    return;
                }
                try {
                    final WatchKey key = watchService.take();
                    if (key == null) {
                        continue;
                    }
                    key.pollEvents();
                    if (!key.reset()) {
                        GLog.severe("Can't reset watch key. Exiting!");
                        return;
                    }
                } catch (final InterruptedException e) {
                    // We out!
                    return;
                }
            }
        } catch (final IOException e) {
            GLog.severe(e);
        }
    }

    private Exception onSourceChange() {
        try {
            final String mainClassName = getClassName(mainJavaFilename);
            final List<JavaFile> javaFiles = gatherJavaFiles(watchedPath);
            final Coalescer coalescer = new Coalescer(javaFiles, mainClassName);
            final String newCoalescedClass = coalescer.coalesce();
            final boolean compileSuccess = doesClassCompile(mainClassName, newCoalescedClass);
            if (compileSuccess) {
                synchronized (coalescedClass) {
                    coalescedClass.value = newCoalescedClass;
                }
            }
            display.setInputFiles(mainJavaFilename, javaFiles);
            display.setCompilationResult(newCoalescedClass, compileSuccess);
            display.print();
        } catch (final Exception e) {
            return e;
        }
        return null;
    }

    private String getClassName(final String filename) {
        return filename.substring(0, filename.length() - 5);
    }

    private List<JavaFile> gatherJavaFiles(final Path directory) throws IOException {
        try (final var fileStream = Files.list(directory)) {
            return fileStream
                    .filter(path -> {
                        final String name = path.getFileName().toString();
                        return Files.isRegularFile(path) && (name.length() > 5) && name.endsWith(".java");
                    })
                    .map(path -> GSupplierWithException.getOrNull(() -> JavaFile.from(path)))
                    .filter(Objects::nonNull)
                    .toList();
        }
    }

    private boolean doesClassCompile(final String className, final String classCode) {
        try {
            final Class<?> clazz = InMemoryJavaCompiler.newInstance().compile(className, classCode);
            return clazz != null;
        } catch (final Exception e) {
            return false;
        }
    }
}
