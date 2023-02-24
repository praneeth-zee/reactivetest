package com.praneeth.reactivetest.handlerfilter;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.praneeth.reactivetest.handler.EmployeeHandler;

import reactor.core.publisher.Mono;

@Component
public class EmployeeHandlerFilterFunction 
    implements HandlerFilterFunction<ServerResponse, ServerResponse> {
  
  @Override
  public Mono<ServerResponse> filter(ServerRequest serverRequest,
      HandlerFunction<ServerResponse> handlerFunction) {
    if (serverRequest.pathVariable("id").equalsIgnoreCase("3")) {
        return ServerResponse.status(HttpStatus.FORBIDDEN).build();
    }
    return handlerFunction.handle(serverRequest);
  }
  
  @Bean
  public RouterFunction<ServerResponse> route(EmployeeHandler handler) {
    return RouterFunctions.route()
        .GET("/employees/{id}", handler::getEmployee)
        .filter(new EmployeeHandlerFilterFunction())
        .build();
  }
}