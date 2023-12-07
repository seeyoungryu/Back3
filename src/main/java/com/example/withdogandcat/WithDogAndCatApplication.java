package com.example.withdogandcat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WithDogAndCatApplication {

	public static void main(String[] args) {
		SpringApplication.run(WithDogAndCatApplication.class, args);
	}

}
