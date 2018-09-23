package com.codegenerator.jgen;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableAutoConfiguration
@SpringBootApplication
@EnableJpaRepositories("generated.repository")
@EntityScan("generated.model")
@ComponentScan({"com.codegenerator.jgen", "generated"})
public class JGenApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(JGenApplication.class, args);

	}

}
