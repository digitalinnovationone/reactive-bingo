package com.bingo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

@SpringBootApplication
@EnableReactiveMongoAuditing
@OpenAPIDefinition(
		info = @Info(title = "Bingo - Web API", version = "1.0.0")
)
public class BingoApplication {
	public static void main(String[] args) {
		SpringApplication.run(BingoApplication.class, args);
	}
}
