package com.thomas.gestPro.controller;

import com.thomas.gestPro.Security.JwtResponse;
import com.thomas.gestPro.Security.TokenRequest;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/current-user")
    public ResponseEntity<JwtResponse> getCurrentUser(@CookieValue(value="accessToken", required=false) String token, HttpServletRequest request) {

        return this.authService.getCurrentUser(token,request);
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
    public ResponseEntity<JwtResponse> authenticateGoogle(@RequestBody TokenRequest tokenRequest) {
        return this.authService.authenticateOAuth(tokenRequest);
    }



}



