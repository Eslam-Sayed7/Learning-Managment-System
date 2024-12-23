package com.Java.LMS.platform;

import com.Java.LMS.platform.config.TestClass;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//
//@SpringBootApplication(exclude = {
//		DataSourceAutoConfiguration.class,
//		HibernateJpaAutoConfiguration.class
//})

@SpringBootApplication
@ComponentScan(basePackages = {"com.Java.LMS.platform"})
public class LmsPlatformApplication {

	public static void main(String[] args) {

//		SpringApplication.run(LmsPlatformApplication.class, args);
		var context = SpringApplication.run(LmsPlatformApplication.class, args);
		TestClass testclass = context.getBean(TestClass.class);
		System.out.println(testclass.sayHello());
	}

//	@Bean
//	CommandLineRunner testRunner(TestService testService) {
//		return args -> testService.testUserCreation();
//	}
}
