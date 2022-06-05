// Copyright 2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.codemasher.server;

import com.glitchybyte.glib.wrapped.GWrappedString;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Code server. Serves a singular file.
 */
@SpringBootApplication
public class Server {

    public static boolean LOCALHOST_ONLY;
    public static int PORT;
    public static String MAIN_JAVA_FILENAME;
    public static GWrappedString COALESCED_CLASS;

    public static void start(final boolean isServingOnLocalhostOnly, final int port,
            final String mainJavaFilename, final GWrappedString coalescedClass) {
        LOCALHOST_ONLY = isServingOnLocalhostOnly;
        PORT = port;
        MAIN_JAVA_FILENAME = mainJavaFilename;
        COALESCED_CLASS = coalescedClass;
        final SpringApplication application = new SpringApplication(Server.class);
        application.setAddCommandLineProperties(false);
        application.run();
    }
}
