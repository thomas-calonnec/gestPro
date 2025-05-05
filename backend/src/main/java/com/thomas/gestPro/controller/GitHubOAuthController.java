package com.thomas.gestPro.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/github")
@CrossOrigin("http://localhost:4200")
public class GitHubOAuthController {

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String clientSecret;

    private final WebClient webClient = WebClient.create();

    @PostMapping("/token")
    public ResponseEntity<?> exchangeCode(@RequestBody Map<String, String> payload, HttpServletResponse response) {
        String code = payload.get("code");
        String redirectUri = "http://localhost:4200/callback";

        var tokenResponse = webClient.post()
                .uri("https://github.com/login/oauth/access_token")
                .header(HttpHeaders.ACCEPT, "application/json")
                .bodyValue(Map.of(
                        "client_id", clientId,
                        "client_secret", clientSecret,
                        "code", code,
                        "redirect_uri", redirectUri
                ))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (tokenResponse != null) {
            String accessToken = (String) tokenResponse.get("access_token");

            // 🔐 Cookie HttpOnly
            ResponseCookie cookie = ResponseCookie.from("gh_token", accessToken)
                    .httpOnly(true)
                    .secure(false) // ✅ true en prod HTTPS
                    .path("/")
                    .maxAge(Duration.ofDays(7))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            var user = webClient.get()
                    .uri("https://api.github.com/user")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            if (user != null) {
                // Ne retourne pas le token au front
                return ResponseEntity.ok(Map.of("user", user));
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@CookieValue("gh_token") String token) {
        var user = webClient.get()
                .uri("https://api.github.com/user")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        return ResponseEntity.ok(user);
    }


}
