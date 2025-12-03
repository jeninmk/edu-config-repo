package org.com.education.gateway.controller;

import org.com.education.gateway.entity.User;
import org.com.education.gateway.security.JwtUtil;
import org.com.education.gateway.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final ReactiveUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService,
                         ReactiveUserDetailsService userDetailsService,
                         JwtUtil jwtUtil) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

@PostMapping("/register")
public Mono<ResponseEntity<Map<String, String>>> register(@RequestBody RegisterRequest request) {
    User.Role role = request.getRole() != null ? request.getRole() : User.Role.USER;

    return Mono.fromCallable(() ->
            userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword(), role)
    ).flatMap(savedUser ->
            userDetailsService.findByUsername(savedUser.getUsername())
                    .map(userDetails -> {
                        String jwt = jwtUtil.generateToken(userDetails);
                        return ResponseEntity.ok(Map.of("token", jwt));
                    })
    ).onErrorResume(e ->
            Mono.just(ResponseEntity.badRequest().body(Map.of("error", e.getMessage())))
    );
}


@PostMapping("/login")
public Mono<ResponseEntity<?>> login(@RequestBody LoginRequest request) {
    return Mono.fromCallable(() ->
            userService.authenticateUser(request.getUsername(), request.getPassword())
    ).flatMap(user -> {
        if (user == null) {
            return Mono.just(ResponseEntity.badRequest().body("Invalid credentials"));
        }

        return userDetailsService.findByUsername(user.getUsername())
                .map(userDetails -> {
                    String jwt = jwtUtil.generateToken(userDetails);
                    return ResponseEntity.ok(Map.of("token", jwt));
                });
    });
}


    @PostMapping("/forgot-password")
    public Mono<ResponseEntity<?>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return Mono.fromCallable(() -> {
            try {
                // In a real application, you would send an email with a reset token
                // For demo purposes, we'll just return a success message
                return ResponseEntity.ok("Password reset email sent to: " + request.getEmail());
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error sending password reset email");
            }
        });
    }

    public static class RegisterRequest {
        private String username;
        private String email;
        private String password;
        private User.Role role;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public User.Role getRole() { return role; }
        public void setRole(User.Role role) { this.role = role; }
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
