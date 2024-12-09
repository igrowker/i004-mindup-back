package com.mindup.gateway.security;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import java.util.List;

    @Component
    public class RouterValidator {

        public static final List<String> openApiEndpoints = List.of(
                "/api/core/register",
                "/api/core/login",
                "/api/core/verify",
                "/api/core/requestPwReset",
                "/api/core/resetPW"
        );

        public boolean isSecured(ServerHttpRequest serverHttpRequest) {
            System.out.println(serverHttpRequest.getURI().getPath());
            return openApiEndpoints.stream().noneMatch(
                    uri -> serverHttpRequest.getURI().getPath().contains(uri));
        }
    }

