package com.bankslips.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication(scanBasePackages="com.bankslips")
@EntityScan("com.bankslips.jpa.entities")
@EnableJpaRepositories("com.bankslips.jpa.repository")
@EnableWebSecurity
public class SprintBootStarter {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SprintBootStarter.class, args);
		
	}
}
