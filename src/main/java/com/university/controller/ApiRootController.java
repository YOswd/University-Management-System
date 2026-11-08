package com.university.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiRootController {
    @GetMapping("/")
    public Map<String, String> root() {
        return Map.of(
                "application", "University Management System API",
                "status", "UP",
                "csrf", "/api/auth/csrf",
                "login", "/api/auth/login");
    }
}
