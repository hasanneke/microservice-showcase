package com.mentoapp.api_gateway.Config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class ApiGatewayConfig {
    @Bean
    public RouteLocator gatewayRoute(RouteLocatorBuilder builder){
        return builder.routes()
                .route(p -> p.path("/api/v1/auth/**", "/api/v1/oauth2/**")
                        .uri("lb://mentoauth")
                )
                .route(p -> p.path("/api/v1/search/text-search","/api/v2/search/text-search")
                        .uri("lb://searchservice")
                )
                .route(p -> p.path("/api/v1/**", "/api/v2/**")
                        .uri("lb://mentoapp")
                )
                .build();
    }
}
