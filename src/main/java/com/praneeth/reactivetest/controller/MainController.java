// package com.praneeth.reactivetest.controller;

// import org.springframework.security.core.annotation.AuthenticationPrincipal;
// import org.springframework.web.bind.annotation.GetMapping;

// import reactor.core.publisher.Mono;

// public class MainController {
    
//     @GetMapping("/")
//     public Mono<String> index(@AuthenticationPrincipal Mono<OAuth2User> oauth2User) {
//        return oauth2User
//         .map(OAuth2User::getName)
//         .map(name -> String.format("Hi, %s", name));
//     }
// }
