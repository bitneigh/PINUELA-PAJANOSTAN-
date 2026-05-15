package com.ws101.pinuela.pajanostan.EcommerceApi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * Constructor injection linking decoupled application context operational components.
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF since the application relies on stateless tokens, not session cookies
                .csrf(csrf -> csrf.disable())

                // 2. Set session management policy strictly to STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Configure explicit public access paths and route block boundaries
                .authorizeHttpRequests(auth -> auth
                        // Whitelist frontend assets and web pages
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/products.html",
                                "/landing.html",
                                "/checkout.html",
                                "/register.html",
                                "/login.html",
                                "/admin.html",
                                "/style.css",
                                "/script.js"
                        ).permitAll()

                        // Allow access to open authentication utilities endpoints
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // Expose products visibility paths dynamically to public users
                        .requestMatchers("/api/v1/products/**").permitAll()

                        // 🌟 CRITICAL FIX: Permit your transactional orders API endpoint path explicitly
                        .requestMatchers("/api/v1/orders/**").permitAll()

                        // Secure remaining transactional routing patterns behind filter guard blocks
                        .anyRequest().authenticated()
                )

                // 4. Gracefully emit raw clean 401 error responses for failed security verifications
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(401, "Unauthorized: Validation processing routine failed.")
                        )
                )

                // 5. Connect data verification parameters context layers mapping engines
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}