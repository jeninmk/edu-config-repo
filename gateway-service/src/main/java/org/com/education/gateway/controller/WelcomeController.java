package org.com.education.gateway.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class WelcomeController {

    private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);

    @GetMapping("/welcome")
    public Mono<ResponseEntity<Map<String, String>>> welcome(ServerHttpRequest request) {
        logger.info("Request received: {} {}", request.getMethod(), request.getPath());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to the Gateway Service!");
        return Mono.just(ResponseEntity.ok(response));
    }
}
