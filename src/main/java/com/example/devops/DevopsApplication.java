package com.example.devops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

@SpringBootApplication
public class DevopsApplication {

	public static void main(String[] args) {
		ConfigurableEnvironment environment = new StandardEnvironment();
		environment.setActiveProfiles("dev");

		SpringApplication sa = new SpringApplication(DevopsApplication.class);
		sa.setEnvironment(environment);
		sa.setAdditionalProfiles("qa", "prod");
		sa.run(args);
	}
}
