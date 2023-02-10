package com.praneeth.reactivetest.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.praneeth.reactivetest.model.Employee;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class EmployeeRepository {
    private List<Employee> employees;

    public EmployeeRepository() {
        employees = new ArrayList<>();
        employees.add(new Employee("1", "Praneeth", "ASDE"));
        employees.add(new Employee("2", "Sagar", "SDE-1"));
        employees.add(new Employee("3", "Raju", "SDE-2"));
    }

    public Flux<Employee> findAllEmployees() {
        return Flux.fromIterable(employees);
    }

    public Mono<Employee> findEmployeeById(String id) {
        return Mono.justOrEmpty(employees.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst()
                .orElse(null));
    }

    public Mono<Employee> createEmployee(Employee employee) {
        if (employees.stream().anyMatch(e -> e.getId().equals(employee.getId()))) {
            employees.stream()
                    .filter(e -> e.getId().equals(employee.getId()))
                    .findFirst()
                    .ifPresent(e -> {
                        e.setFirstName(employee.getFirstName());
                        e.setRole(employee.getRole());
                    });
            return Mono.just(employee);
        }
        employees.add(employee);
        return Mono.just(employee);
    }

    public Mono<Void> deleteEmployee(String id) {
        employees.removeIf(employee -> employee.getId().equals(id));
        return Mono.empty();
    }
}
