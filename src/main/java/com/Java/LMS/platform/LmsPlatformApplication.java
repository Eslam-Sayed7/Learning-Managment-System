package com.Java.LMS.platform;

import com.Java.LMS.platform.config.TestClass;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class
})

public class LmsPlatformApplication {

	public static void main(String[] args) {

		var context = SpringApplication.run(LmsPlatformApplication.class, args);
		TestClass testclass = context.getBean(TestClass.class);
		System.out.println(testclass.sayHello());
	}

}
