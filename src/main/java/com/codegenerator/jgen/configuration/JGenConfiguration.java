package com.codegenerator.jgen.configuration;

import java.io.File;
import java.io.IOException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.TemplateException;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class JGenConfiguration implements WebMvcConfigurer {

	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer() throws IOException, TemplateException {
		FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
		freemarker.template.Configuration config = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
		config.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));
		freeMarkerConfigurer.setConfiguration(config);
		return freeMarkerConfigurer;
	}

}
