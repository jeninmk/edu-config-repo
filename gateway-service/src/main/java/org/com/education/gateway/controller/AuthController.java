package org.com.education.gateway.controller;

import org.com.education.gateway.entity.User;
import org.com.education.gateway.security.JwtUtil;
import org.com.education.gateway.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(request.getUsername(), request.getEmail(),
                    request.getPassword(), User.Role.USER);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Authenticate user
            User user = userService.authenticateUser(request.getUsername(), request.getPassword());
            if (user != null) {
                // Create UserDetails for JWT generation
                UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole().name())
                        .build();
                final String jwt = jwtUtil.generateToken(userDetails);
                Map<String, String> response = new HashMap<>();
                response.put("token", jwt);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body("Invalid credentials - user not found or password incorrect");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login error: " + e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        try {
            // In a real application, you would send an email with a reset token
            // For demo purposes, we'll just return a success message
            return ResponseEntity.ok("Password reset email sent to: " + request.getEmail());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending password reset email");
        }
    }

    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class ForgotPasswordRequest {
        private String email;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}
