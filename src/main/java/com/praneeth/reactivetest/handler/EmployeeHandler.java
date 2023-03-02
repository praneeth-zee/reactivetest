package com.praneeth.reactivetest.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.praneeth.reactivetest.model.Employee;
import com.praneeth.reactivetest.repository.EmployeeRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class EmployeeHandler {

    // REST APIs use controller and service. Functional endpoints use router and handler.

    @Autowired
    private EmployeeRepository employeeRepository;

    public Mono<ServerResponse> getEmployee(ServerRequest request) {
        String name = request.pathVariable("id");
        Mono<Employee> employeeMono = employeeRepository.findEmployeeById(name);
        Employee employee = employeeMono.block();  // convert Mono<Employee> to Employee
        if (employee == null) {
            return ServerResponse.notFound().build();
        } else {
            return ServerResponse.ok().body(BodyInserters.fromValue(employee));
        }
    }
    public Mono<ServerResponse> loadEmployees(ServerRequest request) {
        Flux<Employee> employees = employeeRepository.findAllEmployees();
        employees.subscribe(System.out::println);
        Flux<ServerSentEvent<Employee>> serverSentEvents = employees
                .map(employee -> ServerSentEvent.builder(employee).build());
        return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(serverSentEvents, ServerSentEvent.class);
    }
}