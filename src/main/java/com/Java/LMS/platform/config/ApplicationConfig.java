package com.Java.LMS.platform.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public TestClass testClass () { // seems that you returning constructor
        return new TestClass();
    }
}
