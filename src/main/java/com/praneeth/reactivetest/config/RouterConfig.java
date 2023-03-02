package com.praneeth.reactivetest.config;

import com.praneeth.reactivetest.handler.EmployeeHandler;
import com.praneeth.reactivetest.handlerfilter.EmployeeHandlerFilterFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class RouterConfig {

    @Autowired
    EmployeeHandler employeeHandler;

    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions
                .route(RequestPredicates.GET("/employees"), employeeHandler::loadEmployees)
                .filter(new EmployeeHandlerFilterFunction())
                .andRoute(RequestPredicates.GET("/employee/{id}"), employeeHandler::getEmployee);
    }
}
