package com.auth.authentication.service;

import com.auth.authentication.dto.UserDTO;
import com.auth.authentication.model.User;
import com.auth.authentication.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthFacadeService {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthFacadeService(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<?> registerUser(UserDTO userDTO) {
        try {
            User user = authService.register(userDTO.getUsername(), userDTO.getPassword());
            log.info("Successfully registered user: {}", userDTO.getUsername());
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            log.error("Failed to register user: {}", userDTO.getUsername(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public ResponseEntity<?> loginUser(UserDTO userDTO) {
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
