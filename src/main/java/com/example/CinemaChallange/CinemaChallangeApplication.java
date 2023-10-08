package com.example.CinemaChallange;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Movies scheduler", description = "Movies scheduler"))
public class CinemaChallangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinemaChallangeApplication.class, args);
	}

}
