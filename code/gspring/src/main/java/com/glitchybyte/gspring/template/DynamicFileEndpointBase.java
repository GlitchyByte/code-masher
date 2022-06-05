// Copyright 2021-2022 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.gspring.template;

import com.glitchybyte.gspring.GSpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * Abstract endpoint to serve local files.
 * This endpoint assumes files can be added to or removed from the root being served.
 * If files are not going to change it is more efficient to use {@link StaticFileEndpointBase}.
 */
public abstract class DynamicFileEndpointBase extends FileEndpointBase {

    private static final Logger log = LoggerFactory.getLogger(DynamicFileEndpointBase.class);

    /**
     * Sets up the file endpoint.
     *
     * @param baseUri The base uri of requests. (e.g, "/web")
     * @param sourceRootPath The local file system source root.
     */
    protected DynamicFileEndpointBase(final String baseUri, final Path sourceRootPath) {
        super(baseUri, sourceRootPath, CacheControl.noCache());
    }

    @Override
    protected CompletableFuture<ResponseEntity<StreamingResponseBody>> serveLocalPath(final Path localPath) {
        if (!Files.isRegularFile(localPath)) {
            return NOT_FOUND;
        }
        final MediaType mediaType = GSpringUtils.getMediaType(localPath);
        if (mediaType == null) {
            log.warn("MediaType not found for: {}", localPath);
            return NOT_FOUND;
        }
        return streamingResponse(localPath, mediaType);
    }
}
