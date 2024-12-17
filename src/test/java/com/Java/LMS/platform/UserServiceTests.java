//package com.Java.LMS.platform;
//
//import com.Java.LMS.platform.domain.Entities.Role;
//import com.Java.LMS.platform.domain.Entities.User;
//import com.Java.LMS.platform.infrastructure.repository.RoleRepository;
//import com.Java.LMS.platform.infrastructure.repository.UserRepository;
//import com.Java.LMS.platform.service.impl.CustomUserDetailsService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//public class UserServiceTests {
//
//    @Autowired
//    private CustomUserDetailsService userService;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Test
//    public void testUserCreation() {
//        Role role = new Role();
//        role.setRoleName("ROLE_USER");
//        role.setDescription("Standard User");
//        roleRepository.save(role);
//
//        User newUser = userService.register("testuser", "testuser@example.com","password123", "ROLE_USER");
//
//        assertNotNull(newUser);
//        assertEquals("testuser", newUser.getUsername());
//        assertTrue(passwordEncoder.matches("password123", newUser.getPassword()));
//        assertEquals("ROLE_USER", newUser.getRole().getRoleName());
//    }
//}
