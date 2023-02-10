package com.praneeth.reactivetest.webclient;

import org.springframework.web.reactive.function.client.WebClient;

import com.praneeth.reactivetest.model.Employee;

import reactor.core.publisher.Mono;

public class EmployeeWebClient {

    WebClient client = WebClient.create("http://localhost:8080");

    public Mono<Employee> getEmployeeById(String id) {
        Mono<Employee> employeeMono = client.get()
            .uri("/employees/{id}", "1")
            .retrieve()
            .bodyToMono(Employee.class);
    
        employeeMono.subscribe(System.out::println);
        return employeeMono;
    }

    public Mono<String> getAllEmployees() {
        return client.get()
                .uri("/employees")
                .retrieve()
                .bodyToMono(String.class);
    }

    public Mono<String> updateEmployee(Employee employee) {
        return client.post()
                .uri("/employees/update")
                .bodyValue(employee)
                .retrieve()
                .bodyToMono(String.class);
    }
    
    public Mono<String> test() {
        return client.get()
                .uri("/employees/test")
                .retrieve()
                .bodyToMono(String.class);
    }

}