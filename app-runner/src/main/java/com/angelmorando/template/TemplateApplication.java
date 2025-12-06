package com.angelmorando.template;

// MyBatis mappers are scanned in app-persistence module via MyBatisConfig
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemplateApplication.class, args);
	}

}
