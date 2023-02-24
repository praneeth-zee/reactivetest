package com.praneeth.reactivetest.controller;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.instrument.binder.http.HttpServletRequestTagsProvider;

@RestController
public class AsyncController {
    @GetMapping("/async_result")
    @Async
    public CompletableFuture<String> getResultAsync(HttpServletRequestTagsProvider request) {
        // sleep for 500 ms
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return CompletableFuture.completedFuture("Result is ready!");
    }
}