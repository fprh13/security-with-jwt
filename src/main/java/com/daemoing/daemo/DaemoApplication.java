package com.daemoing.daemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "userAuditorAware")
@SpringBootApplication
public class DaemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaemoApplication.class, args);
	}

}
