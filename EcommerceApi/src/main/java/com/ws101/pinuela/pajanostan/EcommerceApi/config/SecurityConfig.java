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

                .csrf(csrf -> csrf.disable())

                // Configure Endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/products/**", "/api/v1/auth/register").permitAll() // Public access
                        .anyRequest().authenticated()
                )

                // Login Endpoint
                .formLogin(form -> form
                        .loginProcessingUrl("/api/v1/auth/login")

                        .successHandler((request, response, authentication) -> {
                            response.setStatus(200);
                        })

                        .failureHandler((request, response, exception) -> {
                            response.setStatus(401);
                        })
                        .permitAll()

                )

                // Logout Endpoint
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout") // Specific URL for logout
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                // 5. Session Management (Task 1.4 & 2.2)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );

        return http.build();
    }
}