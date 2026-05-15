package com.ws101.pinuela.pajanostan.EcommerceApi.controller;

import com.ws101.pinuela.pajanostan.EcommerceApi.config.JwtUtil;
import com.ws101.pinuela.pajanostan.EcommerceApi.dto.RegisterRequest;
import com.ws101.pinuela.pajanostan.EcommerceApi.model.User;
import com.ws101.pinuela.pajanostan.EcommerceApi.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    /**
     * Constructor injection for optimal dependency management practices.
     */
    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Task 4: Register Endpoint with Validation parameters.
     * Maps the incoming DTO request to the User model, hashes the raw password, and persists it.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest regDto) {

        // Validate uniqueness of the requested identity credential parameters
        if (userRepository.findByUsername(regDto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        // Initialize user model entity allocation mapping
        User user = new User();
        user.setUsername(regDto.getUsername());

        // Apply BCrypt cryptographic hashing structure to the plaintext password parameter
        user.setPassword(passwordEncoder.encode(regDto.getPassword()));
        user.setRole(regDto.getRole());

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    /**
     * Task 10: Stateless Authentication Login Process Endpoint.
     * Verifies JSON body credentials via the AuthenticationManager and issues a signed JWT token block.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {

        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        // Authenticate credentials against current security tracking tables via core manager engine
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // Fetch loaded core account details instance mapping metadata configuration parameters
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Generate a cryptographically secure base64 signature JWT string allocation payload
        final String token = jwtUtil.generateToken(userDetails);

        // Encapsulate the response data inside a clean structured JSON string object wrapper array
        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}