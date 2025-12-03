package org.com.education.gateway.security;

import org.com.education.gateway.entity.User;
import org.com.education.gateway.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Primary
public class CustomReactiveUserDetailsService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    public CustomReactiveUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.justOrEmpty(userRepository.findByUsername(username))
            .map(u -> {
                List<GrantedAuthority> authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_" + u.getRole().name())
                );
                return new org.springframework.security.core.userdetails.User(
                    u.getUsername(),
                    u.getPassword(),
                    authorities
                );
            });
    }
}
