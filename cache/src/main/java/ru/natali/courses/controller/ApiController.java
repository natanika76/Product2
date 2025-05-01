package ru.natali.courses.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    //http://localhost:8080/api/test
    @GetMapping("/test")
    public ResponseEntity<String> testApi() {
        return ResponseEntity.ok("{\"message\": \"API works!\"}");
    }
}
