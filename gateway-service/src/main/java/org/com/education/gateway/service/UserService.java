package org.com.education.gateway.service;

import org.com.education.gateway.entity.User;
import org.com.education.gateway.repository.UserRepository;
import org.com.education.gateway.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
// Register a user - return User instead of String
public User registerUser(String username, String email, String password, User.Role role) {
    User user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(passwordEncoder.encode(password));
    user.setRole(role);

    return userRepository.save(user);
}

// Authenticate a user - return User instead of String
public User authenticateUser(String username, String password) {
    Optional<User> userOpt = userRepository.findByUsername(username);

    if (userOpt.isEmpty()) {
        return null;
    }

    User user = userOpt.get();

    if (!passwordEncoder.matches(password, user.getPassword())) {
        return null;
    }

    return user;
}


    // Used in UserController
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // === Existing CRUD methods ===

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
}

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User userData) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(userData.getUsername());
                    user.setEmail(userData.getEmail());
                    return userRepository.save(user);
                })
                .orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
