package com.glitchybyte.gcc;

import com.glitchybyte.lib.console.GConsole;
import com.sun.nio.file.SensitivityWatchEventModifier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Coalescer implements Runnable {

    private final Path watchedDir;
    private final Path outputFile;
    private int fileLinesPrinted = 0;

    public Coalescer(final Path watchedDir, final Path outputFile) {
        this.watchedDir = watchedDir;
        this.outputFile = outputFile;
    }

    @Override
    public void run() {
        GConsole.println("Watching: %s", GConsole.coloredText(watchedDir.toString(), GConsole.COLOR_BRIGHT_WHITE));
        GConsole.println("Output: %s", GConsole.coloredText(outputFile.toString(), GConsole.COLOR_BRIGHT_WHITE));
        GConsole.flush();
        final WatchService watchService;
        try {
            // Register watcher.
            watchService = FileSystems.getDefault().newWatchService();
            watchedDir.register(watchService,
                    new WatchEvent.Kind[] { StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE },
                    SensitivityWatchEventModifier.HIGH);
            // Watch and update loop.
            while (true) {
                updateOutput();
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

    private void updateOutput() throws IOException {
        GConsole.print(GConsole.cursorUp(fileLinesPrinted));
        for (int i = 0; i < fileLinesPrinted; ++i) {
            GConsole.println(" ".repeat(80));
        }
        GConsole.print(GConsole.cursorUp(fileLinesPrinted));
        fileLinesPrinted = 0;
        final List<Path> files = Files.list(watchedDir)
                .filter(path -> Files.isRegularFile(path) &&
                        (path.getFileName().toString().length() > 5) &&
                        path.getFileName().toString().endsWith(".java"))
                .collect(Collectors.toList());
        final List<JavaClass> javaClasses = readJavaClasses(files);
        writeOutput(javaClasses);
    }

    private List<JavaClass> readJavaClasses(final List<Path> files) throws IOException {
        final List<JavaClass> javaClasses = new ArrayList<>();
        for (final Path file: files) {
            final JavaClass javaClass = readJavaClass(file);
            javaClasses.add(javaClass);
            ++fileLinesPrinted;
        }
        if (fileLinesPrinted == 0) {
            GConsole.println("Read: %s", GConsole.coloredText("Nothing", GConsole.COLOR_BRIGHT_YELLOW));
            GConsole.flush();
            ++fileLinesPrinted;
        }
        return javaClasses;
    }

    private JavaClass readJavaClass(final Path file) throws IOException {
        final String fileName = file.getFileName().toString();
        final String className = fileName.substring(0, fileName.length() - 5);
        final List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        GConsole.println("Read: %s (%d lines)", GConsole.coloredText(fileName, GConsole.COLOR_BRIGHT_GREEN), lines.size());
        GConsole.flush();
        return new JavaClass(className, lines);
    }

    private void writeOutput(final List<JavaClass> javaClasses) {
        // TODO
    }
}
