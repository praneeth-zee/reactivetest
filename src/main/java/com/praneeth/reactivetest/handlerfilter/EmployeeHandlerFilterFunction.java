package com.praneeth.reactivetest.handlerfilter;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

public class EmployeeHandlerFilterFunction 
  implements HandlerFilterFunction<ServerResponse, ServerResponse> {
 
    @Override
    public Mono<ServerResponse> filter(ServerRequest serverRequest,
      HandlerFunction<ServerResponse> handlerFunction) {
        if (serverRequest.pathVariable("id").equalsIgnoreCase("kiran")) {
            return ServerResponse.status(HttpStatus.FORBIDDEN).build();
        }
        return handlerFunction.handle(serverRequest);
    }
}