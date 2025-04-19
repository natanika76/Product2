package ru.natali.taskmanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public  class LogoutController{
    @GetMapping("/logout-basic")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
