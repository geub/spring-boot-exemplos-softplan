package br.com.softplan.unj.exemplo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableAutoConfiguration()
@ComponentScan("br.com.softplan.unj")
public class UnjExemploApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(UnjExemploApplication.class, args);
	}

}
