// Copyright 2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.codemasher.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * Code server. Serves a singular file.
 */
@SpringBootApplication
public class Server {

    public static final String LOCALHOST_ONLY = "localhostOnly";
    public static final String PORT = "port";

    public static void start(final boolean isServingOnLocalhostOnly, final int port) {
        final SpringApplication application = new SpringApplication(Server.class);
        application.setAddCommandLineProperties(false);
        application.setDefaultProperties(Map.of(
                LOCALHOST_ONLY, isServingOnLocalhostOnly,
                PORT, port
        ));
        application.run();
    }
}
