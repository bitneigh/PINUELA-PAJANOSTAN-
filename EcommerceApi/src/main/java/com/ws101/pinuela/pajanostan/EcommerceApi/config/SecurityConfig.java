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
                        // 1. Payagan ang lahat na ma-access ang static frontend UI pages at assets
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

                        // 2. Payagan ang public registration endpoint
                        .requestMatchers("/api/v1/auth/register").permitAll()

                        // 3. Lahat ng secured API requests, kailangan ng authenticated session cookie
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