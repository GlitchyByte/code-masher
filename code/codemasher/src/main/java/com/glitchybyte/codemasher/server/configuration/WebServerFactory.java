// Copyright 2021-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.codemasher.server.configuration;

import com.glitchybyte.codemasher.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class WebServerFactory implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    private static final Logger log = LoggerFactory.getLogger(WebServerFactory.class);

    @Override
    public void customize(final ConfigurableWebServerFactory factory) {
        // Address.
        final InetAddress address = Server.LOCALHOST_ONLY ? InetAddress.getLoopbackAddress() : null;
        if (address !=  null) {
            log.info("Using address: {}", address);
            factory.setAddress(address);
        }
        // Port.
        log.info("Using port: {}", Server.PORT);
        factory.setPort(Server.PORT);
    }
}
