package com.ws101.pinuela.pajanostan.EcommerceApi.controller;

import com.ws101.pinuela.pajanostan.EcommerceApi.dto.RegisterRequest;
import com.ws101.pinuela.pajanostan.EcommerceApi.model.User;
import com.ws101.pinuela.pajanostan.EcommerceApi.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Task 4: Register Endpoint with Validation.
     * Uses RegisterRequest DTO to ensure username and password follow the rules.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest regDto) {

        // Check if username already exists (Optional but good practice)
        if (userRepository.findByUsername(regDto.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        // Mapping DTO to Entity
        User user = new User();
        user.setUsername(regDto.getUsername());
        // Hashing the password (Requirement 4.1.2)
        user.setPassword(passwordEncoder.encode(regDto.getPassword()));
        user.setRole(regDto.getRole());

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }
}