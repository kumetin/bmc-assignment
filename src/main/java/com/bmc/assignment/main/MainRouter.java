package com.bmc.assignment.main;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration(proxyBeanMethods = false)
public class MainRouter {
    @Bean
    public RouterFunction<ServerResponse> route(MainHandler mainHandler){
        return RouterFunctions
            .route(GET("/titanicFairPricePercentilesHistogram").and(accept(MediaType.APPLICATION_JSON)),
                mainHandler::titanicFairPricePercentilesHistogram);
    }
}
