// Copyright 2020-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.codemasher;

import com.glitchybyte.codemasher.masher.Masher;
import com.glitchybyte.codemasher.server.Server;
import com.glitchybyte.glib.GShutdownMonitor;
import com.glitchybyte.glib.console.GConsole;
import com.glitchybyte.glib.wrapped.GWrappedString;
import picocli.CommandLine;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CodeMasher - Java coalescer for CodinGame.
 *
 * <p>Coalesces Java files into a single file usable for CodinGame bots.
 */
@CommandLine.Command(name = "codemasher", mixinStandardHelpOptions = true, version = "codemasher 3.1.0",
        description = "Coalesces Java files into a single file usable for CodinGame bots.",
        footer = "Use @|bold Ctrl + C|@ to exit.")
public final class App implements Callable<Integer> {

    /**
     * Entry point.
     *
     * @param args Command line arguments.
     */
    public static void main(final String[] args) {
        final CommandLine.Help.ColorScheme colorScheme = new CommandLine.Help.ColorScheme.Builder()
                .commands(CommandLine.Help.Ansi.Style.bold)
                .options(CommandLine.Help.Ansi.Style.fg_cyan)
                .parameters(CommandLine.Help.Ansi.Style.fg_cyan)
                .optionParams(CommandLine.Help.Ansi.Style.fg_black, CommandLine.Help.Ansi.Style.bold, CommandLine.Help.Ansi.Style.italic)
                .errors(CommandLine.Help.Ansi.Style.fg_red, CommandLine.Help.Ansi.Style.bold)
                .stackTraces(CommandLine.Help.Ansi.Style.italic)
                .applySystemProperties()
                .build();
        final int exitCode = new CommandLine(new App())
                .setColorScheme(colorScheme)
                .execute(args);
        System.exit(exitCode);
    }

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(names = { "-m", "--main" }, paramLabel = "MAIN_JAVA_FILENAME", defaultValue = "Player.java",
            description = "Main Java filename. Default is @|bold ${DEFAULT-VALUE}|@.")
    private String mainJavaFilename;

    @CommandLine.Option(names = { "-w", "--watched" }, paramLabel = "WATCHED_DIR", required = true,
            description = "Source directory with changing source Java files.")
    private Path watchedPath;

    @CommandLine.Option(names = { "-l", "--localhost" }, defaultValue = "false",
            description = "If it should bind to localhost only. Default is @|bold ${DEFAULT-VALUE}|@.")
    private boolean bindServerToLocalhostOnly;

    @CommandLine.Option(names = { "-p", "--port" }, defaultValue = "10101",
            description = "Port to serve the mashed code. Default is @|bold ${DEFAULT-VALUE}|@.")
    private int serverPort;

    private final GShutdownMonitor shutdownMonitor = GShutdownMonitor.createShutdownMonitor();
    private final GWrappedString coalescedClass = new GWrappedString();

    @Override
    public Integer call() {
        validate();
        Server.start(bindServerToLocalhostOnly, serverPort, mainJavaFilename, coalescedClass);
        final ExecutorService pool = Executors.newSingleThreadExecutor();
        final MiniDisplay miniDisplay = new MiniDisplay(bindServerToLocalhostOnly, serverPort);
        pool.execute(new Masher(watchedPath, mainJavaFilename, coalescedClass, miniDisplay));
        try {
            shutdownMonitor.hold();
            pool.shutdownNow();
            if (pool.awaitTermination(2, TimeUnit.SECONDS)) {
                GConsole.println("%nGood luck!");
                GConsole.flush();
            }
        } catch (final InterruptedException e) {
            // No-op.
        }
        return 0;
    }

    private void validate() {
        // Watched path must be an existing directory.
        if (!Files.isDirectory(watchedPath)) {
            throw new CommandLine.ParameterException(spec.commandLine(), "WATCHED_DIR must exist.");
        }
        // Watched path must be readable to this user.
        if (!Files.isReadable(watchedPath)) {
            throw new CommandLine.ParameterException(spec.commandLine(), "Can't read from WATCHED_DIR.");
        }
        // Main file must be a java file.
        if (!mainJavaFilename.endsWith(".java")) {
            throw new CommandLine.ParameterException(spec.commandLine(), "MAIN_JAVA_FILENAME must be a Java file.");
        }
        // Main file must exist.
        if (!Files.isRegularFile(watchedPath.resolve(mainJavaFilename))) {
            throw new CommandLine.ParameterException(spec.commandLine(), "MAIN_JAVA_FILENAME must exist in WATCHED_DIR.");
        }
    }
}
