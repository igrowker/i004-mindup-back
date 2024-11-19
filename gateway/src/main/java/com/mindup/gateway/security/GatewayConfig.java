package com.mindup.gateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final CustomJwtAuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("chat", r -> r.path("/api/message/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://chat"))
                .route("core", r -> r.path("/api/core/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://core"))
                .build();
    }

}
