package org.com.education.gateway.security;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import org.com.education.gateway.security.JwtUtil;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
    private final ReactiveUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, ReactiveUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        final String jwt = authHeader.substring(7);
        final String username = jwtUtil.extractUsername(jwt);

        if (username != null) {
            return userDetailsService.findByUsername(username)
                    .filter(userDetails -> jwtUtil.validateToken(jwt, userDetails))
                    .flatMap(userDetails -> {
                        var authToken = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                        // Mutate request to forward Authorization header
                        ServerHttpRequest mutatedRequest = exchange.getRequest()
                                .mutate()
                                .header("Authorization", "Bearer " + jwt)
                                .build();

                        ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();

                        return chain.filter(mutatedExchange)
                                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authToken));
                    })
                    .switchIfEmpty(chain.filter(exchange));
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // High priority
    }
}
