package com.praneeth.reactivetest.handlerfilter;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

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
}