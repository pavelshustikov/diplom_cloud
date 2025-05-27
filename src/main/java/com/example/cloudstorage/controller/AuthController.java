package com.example.cloudstorage.controller;

import com.example.cloudstorage.dto.AuthRequest;
import com.example.cloudstorage.dto.AuthResponse;
import com.example.cloudstorage.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        String token = authService.login(request.getLogin(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}