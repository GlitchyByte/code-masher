// Copyright 2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.gspring;

import org.apache.tika.Tika;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * Utils for common Spring operations.
 */
public final class GSpringUtils {

    /**
     * General use Tika instance.
     */
    public static final Tika TIKA = new Tika();

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

    /**
     * Returns the MediaType of the file at the given path.
     *
     * @param path Path to file.
     * @return The MediaType of the file at the given path, or null on any error.
     */
    public static MediaType getMediaType(final Path path) {
        if (!Files.isRegularFile(path)) {
            return null;
        }
        final String mimeType;
        try {
            mimeType = TIKA.detect(path);
        } catch (final IOException e) {
            return null;
        }
        return MediaType.parseMediaType(mimeType);
    }

    /**
     * Returns the MediaType of the document with the given name and bytes.
     *
     * @param name Document name.
     * @param bytes Document bytes.
     * @return The MediaType of the document.
     */
    public static MediaType getMediaType(final String name, final byte[] bytes) {
        final String mimeType = TIKA.detect(bytes, name);
        return MediaType.parseMediaType(mimeType);
    }

    /**
     * Returns the MediaType of the document with the given name and contents.
     *
     * @param name Document name.
     * @param contents Document contents.
     * @return The MediaType of the document.
     */
    public static MediaType getMediaType(final String name, final String contents) {
        final String mimeType = TIKA.detect(contents.getBytes(StandardCharsets.UTF_8), name);
        return MediaType.parseMediaType(mimeType);
    }

    private GSpringUtils() {
        // Hiding constructor.
    }
}
