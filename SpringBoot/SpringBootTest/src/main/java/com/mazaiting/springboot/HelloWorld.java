package com.mazaiting.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class HelloWorld {

	@RequestMapping("/")
	String home() {
		return "Hello mazaiting";
	}
	
	public static void main(String[] args) {
		SpringApplication.run(HelloWorld.class, args);
	}
}
