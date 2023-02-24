package com.praneeth.reactivetest.controller;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.praneeth.reactivetest.model.Employee;
import com.praneeth.reactivetest.repository.EmployeeRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/slow-service-employees")
    private Flux<Employee> slowServiceEmployees() {
        return employeeRepository.findAllEmployees().delayElements(Duration.ofMillis(2000));
    }
    @GetMapping("/get-employees-slowly")
    private Flux<Employee> getEmployeesSlowly() {
        final String uri = "http://localhost:8080/employees/slow-service-employees";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Employee>> response = restTemplate.exchange(
            uri, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Employee>>() {} );
        List<Employee> result = response.getBody();
        return Flux.fromIterable(result);
    }
    
    @GetMapping("/get-employees-non-blocking")
    private Flux<Employee> getEmployeesNonBlocking() {
        final String uri = "http://localhost:8080/employees/slow-service-employees";
        Flux<Employee> employeeFlux = WebClient.create()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(Employee.class);
        // employeeFlux.subscribe(System.out::println);
        return employeeFlux;
    }

    // ___________________________________________________________________

    @GetMapping // for /employees
    private Flux<Employee> getAllEmployees() {
        return employeeRepository.findAllEmployees();
    }

    @GetMapping("/{id}")
    private Mono<Employee> getEmployeeById(@PathVariable String id) {
        return employeeRepository.findEmployeeById(id);
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

    // static routing

    @Bean
    public RouterFunction<ServerResponse> htmlRouter(@Value("classpath:/public/index.html") Resource html) {
        return RouterFunctions.route(RequestPredicates.GET("/"),
                request -> ServerResponse.ok().contentType(MediaType.TEXT_HTML).bodyValue(html));
    }

    @Bean
    public RouterFunction<ServerResponse> imgRouter() {
        return RouterFunctions.resources("/img/**", new ClassPathResource("/static/img/"));
    }

    // error handling

    private Mono<String> testError(ServerRequest request) {
        try {
            return Mono.just("Hello, " + request.queryParam("name").get());
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
    
    public Mono<ServerResponse> handleRequest(ServerRequest request) {
        return testError(request)
                .onErrorReturn("Hello Stranger")
                .flatMap(s -> ServerResponse.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .bodyValue(s));
    }

    // Default Return Status
    @GetMapping(
        value = "/ok",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Flux<String> ok() {
        return Flux.just("ok");
    }

    // Changing the Status Programmatically
    // response.setStatusCode(HttpStatus.ACCEPTED);
    // return Flux.just("accepted");

}