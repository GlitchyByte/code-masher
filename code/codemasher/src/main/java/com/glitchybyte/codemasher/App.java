// Copyright 2020-2021 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.codemasher;

import com.glitchybyte.glib.GStrings;
import com.glitchybyte.glib.GSystem;
import com.glitchybyte.glib.console.GConsole;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Java Coalescer for CodinGame.
 * <p>
 * Coalesces a group of Java files into a single file usable on CodinGame.
 */
public final class App implements Runnable {

    /**
     * Entry point.
     *
     * @param args Command line arguments.
     */
    public static void main(final String[] args) {
        final Path watchedPath = args.length == 0 ? null : Path.of(args[0]).normalize();
        final Path outputPath = args.length == 0 ? null : Path.of(args[1]).normalize();
        verifyParameters(watchedPath, outputPath);
        GConsole.println(GStrings.format("Use %s to exit.", GConsole.coloredText("Ctrl + C", GConsole.COLOR_BRIGHT_WHITE)));
        final App app = new App(watchedPath, outputPath);
        app.run();
    }

    private static void verifyParameters(final Path watchedPath, final Path outputPath) {
        final String cWatched = GConsole.coloredText("WATCHED_DIR", GConsole.COLOR_BRIGHT_WHITE);
        final String cOutput = GConsole.coloredText("OUTPUT_FILE", GConsole.COLOR_BRIGHT_WHITE);
        if ((watchedPath == null) || (outputPath == null)) {
            GConsole.println("jc %s %s", cWatched, cOutput);
            GConsole.println("  %s: Source directory with changing source Java files.", cWatched);
            GConsole.println("  %s: Destination coalesced file.", cOutput);
            GConsole.flush();
            System.exit(1);
        }
        final Path filenamePath = outputPath.getFileName();
        final String cFilename = GConsole.coloredText(filenamePath.toString(), GConsole.COLOR_BRIGHT_CYAN);
        if (!Files.isDirectory(watchedPath)) {
            GConsole.println("%s doesn't exist.", cWatched);
            GConsole.flush();
            System.exit(1);
        }
        if (!Files.isReadable(watchedPath)) {
            GConsole.println("Can't read from %s.", cWatched);
            GConsole.flush();
            System.exit(1);
        }
        if (!outputPath.getFileName().toString().endsWith(".java") || Files.isDirectory(outputPath)) {
            GConsole.println("%s must be a java file.", cOutput);
            GConsole.flush();
            System.exit(1);
        }
        if (!Files.isRegularFile(watchedPath.resolve(filenamePath))) {
            GConsole.println("%s doesn't exist in %s.", cFilename, cWatched);
            GConsole.flush();
            System.exit(1);
        }
        if (outputPath.getParent().equals(watchedPath)) {
            GConsole.println("%s can't be in %s.", cOutput, cWatched);
            GConsole.flush();
            System.exit(1);
        }
        if (!Files.isDirectory(outputPath.getParent())) {
            GConsole.println("%s directory doesn't exist.", cOutput);
            GConsole.flush();
            System.exit(1);
        }
        if (!Files.isWritable(outputPath.getParent())) {
            GConsole.println("Can't write to %s directory.", cOutput);
            GConsole.flush();
            System.exit(1);
        }
    }

    private final Path watchedPath;
    private final Path outputPath;

    public App(final Path watchedPath, final Path outputPath) {
        this.watchedPath = watchedPath;
        this.outputPath = outputPath;
    }

    @Override
    public void run() {
        final ExecutorService pool = Executors.newSingleThreadExecutor();
        pool.execute(new Reactor(watchedPath, outputPath));
        try {
            GSystem.waitForSigInt();
            pool.shutdownNow();
            if (pool.awaitTermination(2, TimeUnit.SECONDS)) {
                GConsole.println("%nGood luck!");
                GConsole.flush();
            }
        } catch (final InterruptedException e) {
            // No-op.
        }
    }
}
