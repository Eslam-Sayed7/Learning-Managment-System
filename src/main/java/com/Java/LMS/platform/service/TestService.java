package com.Java.LMS.platform.service;

import com.Java.LMS.platform.domain.Entities.Role;
import com.Java.LMS.platform.domain.Entities.User;
import com.Java.LMS.platform.infrastructure.repository.RoleRepository;
import com.Java.LMS.platform.infrastructure.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TestService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public TestService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void testUserCreation() {
        Role userRole = roleRepository.findByRoleName("ROLE_STUDENT");

        User newUser = new User();
        newUser.setUsername("testuser");
        newUser.setEmail("testuser@example.com");
        newUser.setPassword(passwordEncoder.encode("password123"));
        newUser.setRole(userRole);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setUpdatedAt(LocalDateTime.now());
        newUser.setActive(true);

        userRepository.save(newUser);

        System.out.println("User created successfully!");
    }

}
