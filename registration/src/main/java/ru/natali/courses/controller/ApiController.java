package ru.natali.courses.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/test")
    public ResponseEntity<String> testApi() {
        return ResponseEntity.ok("{\"message\": \"API works!\"}");
    }
}
