package com.thomas.gestPro.controller;

import com.thomas.gestPro.Security.TokenRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping("/oauth-success")
    public String login(OAuth2AuthenticationToken authentication) {
        if (authentication == null) return "non authentifié";

        OAuth2User user = authentication.getPrincipal();
        String login = user.getAttribute("login"); // GitHub
        return "Bienvenue " + login;
    }
    @GetMapping("/dashboard")
    public String dashboard(OAuth2AuthenticationToken authentication) {
        OAuth2User user = authentication.getPrincipal();
        String login = user.getAttribute("login"); // le username GitHub
        return "Bienvenue " + login;
    }
}
