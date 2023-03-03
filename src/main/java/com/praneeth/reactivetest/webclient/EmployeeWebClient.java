package com.praneeth.reactivetest.webclient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.praneeth.reactivetest.model.Employee;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

public class EmployeeWebClient {

    private WebClient client;

    public EmployeeWebClient(WebClient client) {
        this.client = client;
    }

    public static String LOCALHOST_URL = "http://localhost:8080";

    public EmployeeWebClient create() {
        HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .responseTimeout(Duration.ofMillis(5000))
            .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                    .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

        WebClient client = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();

        return new EmployeeWebClient(client);
    }

    public static WebClient.Builder bindToServer() {
        HttpClient httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .responseTimeout(Duration.ofMillis(5000))
            .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
        
        return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(httpClient));
    }    

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