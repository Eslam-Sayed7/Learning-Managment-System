package com.Java.LMS.platform.adapters.controller;

import com.Java.LMS.platform.config.Security.JWTGenerator;
import com.Java.LMS.platform.infrastructure.repository.RoleRepository;
import com.Java.LMS.platform.infrastructure.repository.UserRepository;
import com.Java.LMS.platform.service.EmailService;
import com.Java.LMS.platform.service.UserService;
import com.Java.LMS.platform.service.dto.Auth.AuthServiceResult;
import com.Java.LMS.platform.service.dto.Auth.LoginRequestModel;
import com.Java.LMS.platform.service.dto.Auth.RegisterRequestModel;
import com.Java.LMS.platform.service.dto.Auth.LoginResponse;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTGenerator jwtGenerator;
    private final EmailService emailService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          EmailService emailService,RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                          JWTGenerator jwtGenerator ,UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.emailService = emailService;
        this.userService = userService;
    }

    @PermitAll
    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequestModel loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);

        return new ResponseEntity<>(new LoginResponse(token), HttpStatus.OK);
    }

   @Transactional
    @PostMapping("register")
    public ResponseEntity<AuthServiceResult> register(@RequestBody RegisterRequestModel registerDto) {
        AuthServiceResult result = new AuthServiceResult();
        try {
            result = userService.registerUserAndSyncRole(registerDto);
            if (result.isResultState()){
                // uncomment if you have the right yml file with the service crendtial
//                emailService.sendRegisterEmail(registerDto.getEmail());
                result.setMessage("Registered successfully");
                return new ResponseEntity<AuthServiceResult>(result , HttpStatus.OK);
            }
        } catch (Exception e) {
            result.setMessage(e.getMessage());
        }
        return new ResponseEntity<AuthServiceResult> ( result , HttpStatus.BAD_REQUEST);
   }
}