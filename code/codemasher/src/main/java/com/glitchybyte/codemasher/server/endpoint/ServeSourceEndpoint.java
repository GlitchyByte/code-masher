// Copyright 2021 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

package com.glitchybyte.codemasher.server.endpoint;

import com.glitchybyte.codemasher.server.configuration.AsyncConfiguration;
import com.glitchybyte.gspring.GSpringUtils;
import com.glitchybyte.gspring.template.DynamicFileEndpointBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

@RestController
public class ServeSourceEndpoint {//extends DynamicFileEndpointBase {

    private static final Logger log = LoggerFactory.getLogger(ServeSourceEndpoint.class);
//    private static final String PARAM_SOURCE = "source";

//    @Autowired
//    public ServeSourceEndpoint(final ApplicationArguments arguments) {
//        super("", getSourceRootPath(arguments));
//    }

//    private static Path getSourceRootPath(final ApplicationArguments arguments) {
//        if (arguments.containsOption(PARAM_SOURCE)) {
//            final String webSource = arguments.getOptionValues(PARAM_SOURCE).get(0);
//            return Paths.get(webSource).toAbsolutePath().normalize();
//        } else {
//            throw new IllegalStateException("No '" + PARAM_SOURCE + "' defined.");
//        }
//    }

    @CrossOrigin(origins = "*")
    @Async(AsyncConfiguration.TASK_EXECUTOR_CONTROLLER)
    @GetMapping("/")
    public CompletableFuture<ResponseEntity<StreamingResponseBody>> serveSource(final HttpServletRequest request) {
        return GSpringUtils.createApiJsonStreamingResponse("{ \"status\": \"ok\" }");
//        return serveFile(request.getRequestURI());
    }
}
