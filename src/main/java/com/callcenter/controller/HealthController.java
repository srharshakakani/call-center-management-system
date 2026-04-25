package com.callcenter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String home() {
        return "Call Center App is Running 🚀";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}