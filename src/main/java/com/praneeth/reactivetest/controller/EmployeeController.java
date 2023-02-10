package com.praneeth.reactivetest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.praneeth.reactivetest.model.Employee;
import com.praneeth.reactivetest.repository.EmployeeRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @GetMapping("/{id}")
    private Mono<Employee> getEmployeeById(@PathVariable String id) {
        return employeeRepository.findEmployeeById(id);
    }

    @GetMapping  // for /employees
    private Flux<Employee> getAllEmployees() {
        return employeeRepository.findAllEmployees();
    }

    // create or update employee
    @PostMapping
    private Mono<Employee> createEmployee(@RequestBody Employee employee) {
        return employeeRepository.createEmployee(employee);
    }

    // delete employee
    @DeleteMapping("/{id}")
    private Mono<Void> deleteEmployee(@PathVariable String id) {
        return employeeRepository.deleteEmployee(id);
    }

    @GetMapping("/test")
    private Mono<String> test() {
        return Mono.just("Hello");
    }
}