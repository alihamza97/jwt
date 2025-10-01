package com.auth.authentication.controller;

import com.auth.authentication.model.User;
import com.auth.authentication.service.AuthService;
import com.auth.authentication.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String username, @RequestParam String password) {
        User user = authService.register(username, password);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        User user = authService.authenticate(username, password);
        if (user != null) {
            String token = jwtUtil.createToken(username);
            return ResponseEntity.ok().body(token);
        }
        return ResponseEntity.badRequest().body("Invalid credentials");
    }
}