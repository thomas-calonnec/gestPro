package com.thomas.gestPro.controller;

import com.thomas.gestPro.Security.JwtResponse;
import com.thomas.gestPro.Security.TokenRequest;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.dto.RegisterRequest;
import com.thomas.gestPro.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

 
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody User user) {
       return this.authService.authenticate(user);
    }

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@RequestBody RegisterRequest registerRequest) {
       return this.authService.register(registerRequest);

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



