package com.codegenerator.jgen;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan("com.codegenerator.jgen")
public class JGenApplication {
	
	
	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(JGenApplication.class, args);
		for (String name: applicationContext.getBeanDefinitionNames()) {
            if(name.contains("dataSource")) {
            	System.out.println(name);
            }
        }
	}

}
