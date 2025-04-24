package com.thomas.gestPro.controller;

import com.thomas.gestPro.Security.JwtResponse;
import com.thomas.gestPro.Security.TokenRequest;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;

    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody User user) {
       return this.authService.authenticate(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<JwtResponse> logout() {
        return this.authService.logout();
    }

    @PostMapping("/revoke-token")
    public ResponseEntity<JwtResponse> refreshAccessToken(@CookieValue(value="revokeToken", required=false) String refreshToken) {
        return this.authService.refresh(refreshToken);
    }

    @PostMapping("/oauth2")
    public ResponseEntity<JwtResponse> authenticateOAuth(@RequestBody TokenRequest tokenRequest) {
        return this.authService.authenticateOAuth(tokenRequest);
    }
    @GetMapping("/dashboard")
    public String dashboard(OAuth2AuthenticationToken authentication) {
        OAuth2User user = authentication.getPrincipal();
        String login = user.getAttribute("login"); // le username GitHub
        return "Bienvenue " + login;
    }
}



