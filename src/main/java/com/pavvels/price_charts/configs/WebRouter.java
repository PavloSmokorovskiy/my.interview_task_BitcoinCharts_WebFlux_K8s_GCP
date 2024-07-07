package com.pavvels.price_charts.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class WebRouter {

    @Bean
    public RouterFunction<ServerResponse> htmlRouter() {
        return RouterFunctions
                .route(RequestPredicates.GET("/"), request ->
                        ServerResponse.ok().bodyValue(new ClassPathResource("static/index.html")));
    }
}
