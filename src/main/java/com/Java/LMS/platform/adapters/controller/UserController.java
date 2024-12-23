package com.Java.LMS.platform.adapters.controller;
import com.Java.LMS.platform.config.Security.JWTGenerator;
import com.Java.LMS.platform.domain.Entities.User;
import com.Java.LMS.platform.infrastructure.repository.RoleRepository;
import com.Java.LMS.platform.infrastructure.repository.UserRepository;
import com.Java.LMS.platform.service.EmailService;
import com.Java.LMS.platform.service.UserService;
import com.Java.LMS.platform.service.dto.Auth.*;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;
    private final EmailService emailService;
    private final UserService userService;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          EmailService emailService, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                          JWTGenerator jwtGenerator, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.emailService = emailService;
        this.userService = userService;
    }

    @PostMapping("password-change")
    public ResponseEntity<AccountResponse> changePassword(@RequestBody AccountRePasswordModel accountData) {

        AccountResponse result = new AccountResponse();
        try {
            result = userService.changePassword(accountData);

            result.setResultState(result.isResultState());
            result.setMessage(result.getMessage());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PermitAll
    @PostMapping("email-change")
    public ResponseEntity<AccountResponse> changemail(@RequestBody AccountReMailModel accountData) {

        AccountResponse result = new AccountResponse();
        try {
//            accountData.setUsername(username);
            result = userService.changeMail(accountData);
            return new ResponseEntity<AccountResponse>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            return new ResponseEntity<AccountResponse> ( result , HttpStatus.BAD_REQUEST);
        }
    }
    @PermitAll
    @PostMapping("username-change")
    public ResponseEntity<AccountResponse> changeUsername(@RequestBody AccountReUsernameModel accountData) {

        AccountResponse result = new AccountResponse();
        try {
            result = userService.changeUsername(accountData);
            return new ResponseEntity<AccountResponse>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            return new ResponseEntity<AccountResponse> ( result , HttpStatus.BAD_REQUEST);
        }
    }
}