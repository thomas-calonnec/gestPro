package com.thomas.gestPro.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Applique CORS à tous les endpoints
                .allowedOrigins("http://localhost:4200")  // Autorise le domaine Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Méthodes HTTP autorisées
                .allowedHeaders("*")  // Autorise tous les headers
                .allowCredentials(true)  // Autorise l'envoi de cookies (ex: JWT dans les cookies)
                .maxAge(3600);  // Durée de la configuration en cache (en secondes)
    }
}