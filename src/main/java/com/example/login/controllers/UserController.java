package com.example.login.controllers;

import com.example.login.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")

public class UserController {
    @GetMapping
    public ResponseEntity<String> getUser() {
        return ResponseEntity.ok("success");
    }
}
