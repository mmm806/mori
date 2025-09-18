package com.example.mori;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MoriApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoriApplication.class, args);
	}

}
