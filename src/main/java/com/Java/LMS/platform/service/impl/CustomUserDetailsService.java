package com.Java.LMS.platform.service.impl;

import com.Java.LMS.platform.domain.Entities.Role;
import com.Java.LMS.platform.domain.Entities.User;
import com.Java.LMS.platform.infrastructure.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadUserById(Long Id) throws ExceptionInInitializerError {
       User user = userRepository.findById(Id).orElseThrow(() -> new RuntimeException("User Not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                mapRoleToAuthority(user.getRole())
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        if (user.getRole() == null) {
            throw new IllegalStateException("Role is not assigned to the user");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                mapRoleToAuthority(user.getRole())
        );
    }

    private Collection<GrantedAuthority> mapRoleToAuthority(Role role) {
        // Ensure role is not null before proceeding
        if (role == null || role.getRoleName() == null) {
            throw new IllegalStateException("Role or role name is null");
        }
        return Collections.singletonList(new SimpleGrantedAuthority(role.getRoleName()));
    }
}
