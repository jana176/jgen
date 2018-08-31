package com.codegenerator.jgen;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan("com.codegenerator.jgen")
public class JGenApplication {
	
	
	public static void main(String[] args) {
		SpringApplication.run(JGenApplication.class, args);

	}

}
