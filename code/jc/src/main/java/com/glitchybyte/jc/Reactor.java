// Copyright 2021-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.jc;

import com.glitchybyte.glib.console.GConsole;
import com.sun.nio.file.SensitivityWatchEventModifier;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Reactor implements Runnable {

    private final Path watchedPath;
    private final Path outputPath;

    /**
     * Creates the reactor.
     *
     * @param watchedPath Watched directory.
     * @param outputPath Output file.
     */
    public Reactor(final Path watchedPath, final Path outputPath) {
        this.watchedPath = watchedPath;
        this.outputPath = outputPath;
    }

    /**
     * Main loop of the thread.
     */
    @Override
    public void run() {
        GConsole.println("Watching: %s", GConsole.coloredText(watchedPath.toString(), GConsole.COLOR_BRIGHT_WHITE));
        GConsole.println("Output: %s", GConsole.coloredText(outputPath.toString(), GConsole.COLOR_BRIGHT_WHITE));
        GConsole.flush();
        try {
            // Register watcher.
            final WatchService watchService = FileSystems.getDefault().newWatchService();
            watchedPath.register(watchService,
                    new WatchEvent.Kind[] {
                            StandardWatchEventKinds.ENTRY_CREATE,
                            StandardWatchEventKinds.ENTRY_DELETE,
                            StandardWatchEventKinds.ENTRY_MODIFY
                    },
                    SensitivityWatchEventModifier.HIGH);
            // Watch and update loop.
            final MiniConsole miniConsole = new MiniConsole();
            while (true) {
                miniConsole.clear();
                final List<JavaFile> javaFiles = gatherJavaFiles(watchedPath);
                miniConsole.printInputFiles(javaFiles);
                final Coalescer coalescer = new Coalescer(javaFiles, getClassName(outputPath));
                final String coalescedClass = coalescer.coalesce();
                Files.writeString(outputPath, coalescedClass);
                if (doesClassCompile(coalescedClass)) {
                    Files.writeString(outputPath, coalescedClass);
                    miniConsole.printOutputFile(outputPath.getFileName().toString(), coalescedClass);
                } else {
                    miniConsole.printOutputFile(outputPath.getFileName().toString(), null);
                }
                try {
                    final WatchKey key = watchService.take();
                    if (key == null) {
                        continue;
                    }
                    key.pollEvents();
                    if (!key.reset()) {
                        GConsole.println("Can't reset watch key. Exiting!");
                        GConsole.flush();
                        return;
                    }
                } catch (final InterruptedException e) {
                    // We out!
                    return;
                }
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private String getClassName(final Path path) {
        final String filename = path.getFileName().toString();
        return filename.substring(0, filename.length() - 5);
    }

    private List<JavaFile> gatherJavaFiles(final Path path) throws IOException {
        final List<Path> javaFilePaths;
        try (final var fileStream = Files.list(path)) {
            javaFilePaths = fileStream
                    .filter(p -> Files.isRegularFile(p) &&
                            (p.getFileName().toString().length() > 5) &&
                            p.getFileName().toString().endsWith(".java"))
                    .toList();
        }
        final List<JavaFile> javaFiles = new ArrayList<>(javaFilePaths.size());
        for (final Path javaFilePath: javaFilePaths) {
            javaFiles.add(JavaFile.from(javaFilePath));
        }
        return javaFiles;
    }

    private boolean doesClassCompile(final String classCode) {
        try {
            final Class<?> clazz = InMemoryJavaCompiler.newInstance().compile("Player", classCode);
            return clazz != null;
        } catch (final Exception e) {
            return false;
        }
    }
}
