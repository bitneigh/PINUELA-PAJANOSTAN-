package com.ws101.pinuela.pajanostan.EcommerceApi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Enable CSRF for form submissions (Requirement sa Task 1.4)
                .csrf(Customizer.withDefaults())

                // 2. Configure Endpoints (Requirement sa Task 1.4)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/products/**", "/api/v1/auth/register").permitAll()
                        .requestMatchers("/api/v1/orders/**").authenticated()
                        .anyRequest().authenticated()
                )

                // 3. Enable Form Login (Requirement sa Task 1.4)
                .formLogin(Customizer.withDefaults())

                // 4. Session Management (Requirement sa Task 1.4)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );

        return http.build();
    }
}