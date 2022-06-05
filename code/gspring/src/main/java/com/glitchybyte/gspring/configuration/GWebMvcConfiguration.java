// Copyright 2021-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.gspring.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.async.CallableProcessingInterceptor;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

/**
 * This class sets up async configuration.
 */
public abstract class GWebMvcConfiguration extends WebMvcConfigurationSupport {

    private static final Logger log = LoggerFactory.getLogger(GWebMvcConfiguration.class);

    private final Executor taskExecutor;

    protected GWebMvcConfiguration(final Executor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    protected void configureAsyncSupport(final AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor((AsyncTaskExecutor) taskExecutor)
                .setDefaultTimeout(30_000)
                .registerCallableInterceptors(getCallableProcessingInterceptor());
        super.configureAsyncSupport(configurer);
    }

    protected CallableProcessingInterceptor getCallableProcessingInterceptor() {
        return new TimeoutCallableProcessingInterceptor() {
            @Override
            public <T> Object handleTimeout(final NativeWebRequest request, final Callable<T> task) throws Exception {
                log.error("Timeout request: {}", request.getContextPath());
                return super.handleTimeout(request, task);
            }
        };
    }
}
