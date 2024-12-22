package com.Java.LMS.platform.adapters.controller;
import com.Java.LMS.platform.config.Security.JWTGenerator;
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

    @PermitAll
    @PostMapping("password-change")
    public ResponseEntity<AccountResponse> changePassword(@RequestBody AccountRePasswordModel accountData) {

        AccountResponse result = new AccountResponse();

        try {
            // Step 1: Validate JWT token
            // Since it already validates in the header this function is not needed
//            if (!jwtGenerator.validateToken(accountData.getToken())) {
//                Map<String, String> errorResponse = new HashMap<>();
//                errorResponse.put("error", "Invalid JWT Token");
//                return new ResponseEntity( errorResponse , HttpStatus.BAD_REQUEST);
//            }
            String username = jwtGenerator.getUsernameFromJWT(accountData.getToken());
            // Step 2: Authenticate old password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            accountData.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Step 3: Apply new password
            accountData.setUsername(username);
            result = userService.changePassword(accountData);

            // Step 4: Generate new token
            String newToken = jwtGenerator.generateToken(authentication);
            result.setToken(newToken);
            return new ResponseEntity<AccountResponse>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            return new ResponseEntity<AccountResponse> ( result , HttpStatus.BAD_REQUEST);
        }
    }
    @PermitAll
    @PostMapping("email-change")
    public ResponseEntity<AccountResponse> changemail(@RequestBody AccountReMailModel accountData) {

        AccountResponse result = new AccountResponse();
        //result = userService.changePassword(accountData);

        try {

            // Step 2: Authenticate password
            String username = jwtGenerator.getUsernameFromJWT(accountData.getToken());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                           username,
                            accountData.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Step 3: Apply new email
            accountData.setUsername(username);
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

            String username = jwtGenerator.getUsernameFromJWT(accountData.getToken());
            // Step 2: Authenticate old password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            accountData.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Step 3: Apply new password
            accountData.setUsername(username);
            result = userService.changeUsername(accountData);

            // Step 4: Generate new token
            String newToken = jwtGenerator.generateToken(authentication);
            result.setToken(newToken);
            return new ResponseEntity<AccountResponse>(result, HttpStatus.OK);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            return new ResponseEntity<AccountResponse> ( result , HttpStatus.BAD_REQUEST);
        }
    }
}