// Copyright 2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.gspring;

import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

/**
 * Utils for common Spring operations.
 */
public final class GSpringUtils {

    /**
     * Returns an api ready response from a given json string.
     *
     * @param json Json string.
     * @return An api ready response.
     */
    public static CompletableFuture<ResponseEntity<String>>
    createApiJsonResponse(final String json) {
        return CompletableFuture.completedFuture(
                ResponseEntity.ok()
                        .cacheControl(CacheControl.noCache())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(json)
        );
    }

    /**
     * Returns an api ready response from a given stream, which is assumed to be writing json output.
     *
     * @param streamingResponseBody Streaming body in json format.
     * @return An api ready response.
     */
    public static CompletableFuture<ResponseEntity<StreamingResponseBody>>
    createApiJsonStreamingResponse(final StreamingResponseBody streamingResponseBody) {
        return CompletableFuture.completedFuture(
                ResponseEntity.ok()
                        .cacheControl(CacheControl.noCache())
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(streamingResponseBody)
        );
    }

    /**
     * Returns an api ready response from a given json string.
     *
     * @param json Json string.
     * @return An api ready response.
     */
    public static CompletableFuture<ResponseEntity<StreamingResponseBody>>
    createApiJsonStreamingResponse(final String json) {
        final StreamingResponseBody streamingResponseBody = outputStream -> {
            try (final Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
                writer.append(json);
            }
        };
        return createApiJsonStreamingResponse(streamingResponseBody);
    }

    private GSpringUtils() {
        // Hiding constructor.
    }
}
