package com.auth.authentication.controller;

import com.auth.authentication.dto.UserDTO;
import com.auth.authentication.model.User;
import com.auth.authentication.security.JwtUtil;
import com.auth.authentication.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {
        log.info("Received registration request for user: {}", userDTO.getUsername());
        try {
            User user = authService.register(userDTO.getUsername(), userDTO.getPassword());
            log.info("Successfully registered user: {}", userDTO.getUsername());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Failed to register user: {}", userDTO.getUsername(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        log.info("Received login request for user: {}", userDTO.getUsername());
        try {
            User user = authService.authenticate(userDTO.getUsername(), userDTO.getPassword());
            if (user != null) {
                String token = jwtUtil.createToken(userDTO.getUsername());
                log.info("Successfully logged in user: {}", userDTO.getUsername());
                return ResponseEntity.ok().body(token);
            }
            log.warn("Invalid credentials for user: {}", userDTO.getUsername());
            return ResponseEntity.badRequest().body("Invalid credentials");
        } catch (Exception e) {
            log.error("Login failed for user: {}", userDTO.getUsername(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
