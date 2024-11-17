package com.mindup.gateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private CustomJwtAuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("chat", r -> r.path("/api/message/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://chat"))
                .route("core", r -> r.path("/api/core/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://core"))
                /*.route("eureka", r -> r.path("/eureka/web")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:8761"))*/
                .build();
    }
}
