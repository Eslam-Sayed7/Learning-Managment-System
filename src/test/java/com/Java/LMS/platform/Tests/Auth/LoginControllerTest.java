//package com.Java.LMS.platform;
////package com.Java.LMS.platform.api.controllers;
//
//import com.Java.LMS.platform.adapters.controller.AuthController;
//import com.Java.LMS.platform.service.impl.CustomUserDetailsService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//class LoginControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Autowired
//    public LoginControllerTest(CustomUserDetailsService userDetailsService , JWTService jwtService) {
//        AuthController authController = new AuthController(userDetailsService , jwtService);
//        this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
//    }
//
//    @Test
//    void testLoginSuccess() throws Exception {
//        String requestBody = "{\"username\": \"testuser\", \"password\": \"testpassword\"}";
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testLoginFailure() throws Exception {
//        String requestBody = "{\"username\": \"wronguser\", \"password\": \"wrongpassword\"}";
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isUnauthorized());
//    }
//}
//
