package com.Java.LMS.platform.adapters.controller;

import com.Java.LMS.platform.domain.Entities.User;
import com.Java.LMS.platform.domain.Models.LoginRequestModel;
import com.Java.LMS.platform.domain.Models.RegisterRequestModel;
import com.Java.LMS.platform.service.CustomUserDetailsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final CustomUserDetailsService userDetailsService;

    public AuthController(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestModel request) {
        userDetailsService.register(request.getUsername(), request.getEmail(), request.getPassword(), request.getRoleName());
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestModel loginRequest) {
        boolean loginSuccess = userDetailsService.loginUser(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        if (loginSuccess) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}
