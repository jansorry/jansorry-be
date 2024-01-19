package com.ssafy.jansorry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JansorryApplication {

	public static void main(String[] args) {
		SpringApplication.run(JansorryApplication.class, args);
	}

}
