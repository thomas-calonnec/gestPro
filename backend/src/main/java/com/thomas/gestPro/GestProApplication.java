package com.thomas.gestPro;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class GestProApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestProApplication.class, args);
	}




}
