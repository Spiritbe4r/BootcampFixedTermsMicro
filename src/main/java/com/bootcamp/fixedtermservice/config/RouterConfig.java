package com.bootcamp.fixedtermservice.config;

import com.bootcamp.fixedtermservice.handlers.FixedTermHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {
    /**
     * Routes router function.
     *
     * @param fixedTermAccountHandler the fixed term account handler
     * @return the router function
     */
    @Bean
    public RouterFunction<ServerResponse> routes(FixedTermHandler fixedTermAccountHandler){

        return route(GET("/api/fixedTermAccount"), fixedTermAccountHandler::findAll)
                .andRoute(GET("/api/fixedTermAccount/{clientIdNumber}"), fixedTermAccountHandler::findByCustomerIdentityNumber)
                .andRoute(GET("/api/fixedTermAccount/account/{accountNumber}"), fixedTermAccountHandler::findByAccountNumber)
                .andRoute(POST("/api/fixedTermAccount"), fixedTermAccountHandler::newFixedTermAccount)
                .andRoute(PUT("/api/fixedTermAccount/{id}"), fixedTermAccountHandler::updateFixedTermAccound)
                .andRoute(DELETE("/api/fixedTermAccount/{id}"), fixedTermAccountHandler::deleteFixedTermAccound);

    }
}
