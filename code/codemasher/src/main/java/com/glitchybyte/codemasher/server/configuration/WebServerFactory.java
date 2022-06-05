// Copyright 2021 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.codemasher.server.configuration;

import com.glitchybyte.codemasher.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class WebServerFactory implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    private static final Logger log = LoggerFactory.getLogger(WebServerFactory.class);

    @Autowired
    private Environment environment;

    @Override
    public void customize(final ConfigurableWebServerFactory factory) {
        final boolean isServingOnLocalhostOnly = environment.getRequiredProperty(Server.LOCALHOST_ONLY, Boolean.class);
        final int serverPort = environment.getRequiredProperty(Server.PORT, Integer.class);
        // Address.
        final InetAddress address = isServingOnLocalhostOnly ? InetAddress.getLoopbackAddress() : null;
        if (address !=  null) {
            log.info("Using address: {}", address);
            factory.setAddress(address);
        }
        // Port.
        log.info("Using port: {}", serverPort);
        factory.setPort(serverPort);
    }
}
