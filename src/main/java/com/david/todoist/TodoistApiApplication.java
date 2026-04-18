package com.david.todoist;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.david.todoist.auth.JWT.JwtService;

@SpringBootApplication
public class TodoistApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoistApiApplication.class, args);
	}
}

