package com.ashgan.bezeqDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BezeqDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BezeqDemoApplication.class, args);
	}

}

