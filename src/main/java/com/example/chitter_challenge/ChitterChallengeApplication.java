package com.example.chitter_challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;

@SpringBootApplication
public class ChitterChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChitterChallengeApplication.class, args);
	}


}