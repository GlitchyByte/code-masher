// Copyright 2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.codemasher.server.endpoint;

import com.glitchybyte.codemasher.server.Server;
import com.glitchybyte.codemasher.server.configuration.AsyncConfiguration;
import com.glitchybyte.gspring.GSpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@RestController
public class ServeSingleSourceEndpoint {

    private static final Logger log = LoggerFactory.getLogger(ServeSingleSourceEndpoint.class);

    private MediaType sourceMediaType = null;

    @CrossOrigin(origins = "*")
    @Async(AsyncConfiguration.TASK_EXECUTOR_CONTROLLER)
    @GetMapping("/**")
    @SuppressWarnings("SynchronizeOnNonFinalField")
    public CompletableFuture<ResponseEntity<StreamingResponseBody>> serveSingleSource(final HttpServletRequest request) {
        final String code;
        synchronized (Server.COALESCED_CLASS) {
            code = Objects.requireNonNullElse(Server.COALESCED_CLASS.value, "// No code :(\n");
        }
        synchronized (this) {
            if (sourceMediaType == null) {
                sourceMediaType = GSpringUtils.getMediaType(Server.MAIN_JAVA_FILENAME, code);
            }
        }
        final StreamingResponseBody streamingResponseBody = outputStream -> {
            try (final Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
                writer.append(code);
            }
        };
        return CompletableFuture.completedFuture(
                ResponseEntity.ok()
                        .cacheControl(CacheControl.noCache())
                        .contentType(sourceMediaType)
                        .body(streamingResponseBody)
        );
    }
}
