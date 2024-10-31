package com.thomas.gestPro.Security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permettre CORS sur tous les chemins
                .allowedOrigins("http://192.168.1.138:4200") // Autoriser Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Méthodes HTTP autorisées
                .allowedHeaders("Authorization", "Content-Type") // Autoriser les en-têtes d'authentification
                //.exposedHeaders("Authorization") // Facultatif si vous devez exposer cet en-tête dans la réponse
                .allowCredentials(true); // Si vous avez des cookies de session (sinon, pas nécessaire)
    }
}