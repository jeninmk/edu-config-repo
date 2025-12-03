package org.com.education.gateway.security;

import org.com.education.gateway.entity.User;
import org.com.education.gateway.repository.UserRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.fromCallable(() -> userRepository.findByUsername(username))
                .flatMap(optionalUser -> {
                    if (optionalUser.isPresent()) {
                        User user = optionalUser.get();
                        return Mono.just(org.springframework.security.core.userdetails.User.builder()
                                .username(user.getUsername())
                                .password(user.getPassword())
                                .roles(user.getRole().name())
                                .build());
                    } else {
                        return Mono.error(new UsernameNotFoundException("User not found: " + username));
                    }
                });
    }
}
