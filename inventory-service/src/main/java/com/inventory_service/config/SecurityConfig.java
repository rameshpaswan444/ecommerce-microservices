package com.inventory_service.config;

import com.inventory_service.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth

                        // Public APIs
                        .requestMatchers(HttpMethod.GET,
                                "/api/inventories/**").permitAll()

                        // Reserve inventory - authenticated users
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/inventories/*/reserve"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/inventories/*/release"
                        ).authenticated()

                        // Admin only
                        .requestMatchers(HttpMethod.POST,
                                "/api/inventories/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/inventories/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/inventories/**").hasRole("ADMIN")

                        .anyRequest().authenticated())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
