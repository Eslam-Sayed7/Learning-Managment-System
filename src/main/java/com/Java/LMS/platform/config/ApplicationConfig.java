package com.Java.LMS.platform.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class ApplicationConfig {
    @Bean
    public TestClass testClass () { // seems that you returning constructor
        return new TestClass();
    }
}
