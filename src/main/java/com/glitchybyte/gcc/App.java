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

import com.glitchybyte.lib.GStrings;
import com.glitchybyte.lib.GSystem;
import com.glitchybyte.lib.console.GConsole;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Glitchy CodinGame Coalescer.
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
        final Path watchedPath = args.length == 0 ? null : Path.of(args[0]).toAbsolutePath().normalize();
        final Path outputPath = args.length == 0 ? null : Path.of(args[1]).toAbsolutePath().normalize();
        verifyParameters(watchedPath, outputPath);
        final App app = new App(watchedPath, outputPath);
        app.run();
    }

    private static void verifyParameters(final Path watchedPath, final Path outputPath) {
        final String coloredWatched = GConsole.coloredText("dir_to_watch", GConsole.COLOR_BRIGHT_WHITE);
        final String coloredOutput = GConsole.coloredText("output_file", GConsole.COLOR_BRIGHT_WHITE);
        if ((watchedPath == null) || (outputPath == null)) {
            GConsole.println("Needs two args: %s and %s.", coloredWatched, coloredOutput);
            GConsole.flush();
            System.exit(1);
        }
        if (!Files.isDirectory(watchedPath)) {
            GConsole.println("%s doesn't exist.", coloredWatched);
            GConsole.flush();
            System.exit(1);
        }
        if (!Files.isReadable(watchedPath)) {
            GConsole.println("Can't read %s.", coloredWatched);
            GConsole.flush();
            System.exit(1);
        }
        if (!Files.isDirectory(outputPath.getParent())) {
            GConsole.println("Parent dir of %s doesn't exist.", coloredOutput);
            GConsole.flush();
            System.exit(1);
        }
        if (!Files.isWritable(outputPath.getParent())) {
            GConsole.println("Can't write to parent dir of %s.", coloredOutput);
            GConsole.flush();
            System.exit(1);
        }
        if (Files.exists(outputPath)) {
            if (!Files.isRegularFile(outputPath)) {
                GConsole.println("%s is not a file.", coloredOutput);
                GConsole.flush();
                System.exit(1);
            }
            if (!Files.isWritable(outputPath)) {
                GConsole.println("Can't write to %s.", coloredOutput);
                GConsole.flush();
                System.exit(1);
            }
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
        pool.execute(new Coalescer(watchedPath, outputPath));
        try {
            GSystem.waitForSigInt();
            pool.shutdownNow();
            pool.awaitTermination(2, TimeUnit.SECONDS);
        } catch (final InterruptedException e) {
            // No-op.
        }
        GConsole.println("%nGood luck!");
        GConsole.flush();
    }
}
