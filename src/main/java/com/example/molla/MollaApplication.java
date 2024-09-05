package com.example.molla;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MollaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MollaApplication.class, args);
	}
}
