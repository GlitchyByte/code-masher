// Copyright 2021 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.codemasher.server.configuration;

import com.glitchybyte.gspring.configuration.GWebMvcConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;

@Configuration
public class WebMvcConfiguration extends GWebMvcConfiguration {

    private static final Logger log = LoggerFactory.getLogger(WebMvcConfiguration.class);

    @Autowired
    protected WebMvcConfiguration(final Executor taskExecutor) {
        super(taskExecutor);
    }
}
