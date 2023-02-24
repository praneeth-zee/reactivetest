package com.praneeth.reactivetest.controller;

import java.time.Duration;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.praneeth.reactivetest.webclient.EmployeeWebClient;

import reactor.core.publisher.Mono;

@RestController
public class WebFluxController {

    // to delay output by 2.5 seconds
    @GetMapping("/flux_result")
    public Mono<String> getResult(ServerHttpRequest request) {
      EmployeeWebClient client = EmployeeWebClient.create();
      // Mono<Employee> employeeMono = client.getEmployeeById("1");
      Mono<String> testMono = client.test();
      testMono.subscribe(System.out::println);
      
      return Mono.defer(() -> Mono.just("Result is ready!"))
        .delaySubscription(Duration.ofMillis(2500));
    }
}