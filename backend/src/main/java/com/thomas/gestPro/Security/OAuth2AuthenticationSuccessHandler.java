package com.thomas.gestPro.Security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Date;
import java.util.Map;

public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    private String generateJwt(String email, String name) {
        // 👉 JWT de base (pour test)
        return Jwts.builder()
                .setSubject(email)
                .claim("name", name)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1h
                .signWith(SignatureAlgorithm.HS256, "secretKey") // 🔐 À externaliser en prod
                .compact();
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws java.io.IOException, ServletException {
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        Map<String, Object> userAttributes = oauthToken.getPrincipal().getAttributes();

        String email = (String) userAttributes.get("email");
        String name = (String) userAttributes.get("name");

        // 1. Générer un JWT (ou autre token sécurisé)
        String token = generateJwt(email, name);

        // 2. Rediriger vers Angular (ex: dans une popup)
        String redirectUrl = "http://localhost:4200/oauth-success?token=" + token;
        response.sendRedirect(redirectUrl);
    }
}
