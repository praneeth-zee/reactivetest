package com.praneeth.reactivetest.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.praneeth.reactivetest.model.Employee;
import com.praneeth.reactivetest.repository.EmployeeRepository;

import reactor.core.publisher.Mono;

@Component
public class EmployeeHandler {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Mono<ServerResponse> getEmployee(ServerRequest request) {
        String name = request.pathVariable("id");
        Employee employee = employeeRepository.findEmployeeById(name).block();
        // .block() to convert Mono<Employee> to Employee object
        if (employee == null) {
            return ServerResponse.notFound().build();
        } else {
            return ServerResponse.ok().body(BodyInserters.fromValue(employee));
        }
    }
}