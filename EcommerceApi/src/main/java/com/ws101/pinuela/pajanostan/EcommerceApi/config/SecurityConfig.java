package com.ws101.pinuela.pajanostan.EcommerceApi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                // Configure Endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/products.html",
                                "/checkout.html",
                                "/admin.html",
                                "/login.html",
                                "/style.css",
                                "/script.js"
                        ).permitAll()

                        .requestMatchers("/api/v1/auth/register").permitAll()

                        // Lahat ng secured API requests, need ng authenticated session cookie
                        .requestMatchers("/api/v1/products/**").authenticated()
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

                // Session Management
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );

        return http.build();
    }
}