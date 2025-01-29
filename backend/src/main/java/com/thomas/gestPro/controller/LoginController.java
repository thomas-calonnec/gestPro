package com.thomas.gestPro.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.thomas.gestPro.Security.JwtResponse;
import com.thomas.gestPro.Security.JwtResponseGoogle;
import com.thomas.gestPro.Security.JwtTokenUtil;
import com.thomas.gestPro.Security.TokenRequest;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.service.AuthService;
import com.thomas.gestPro.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;



@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Autowired
    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService, UserService userService) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody User user, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // System.out.println("user : " + user.getUsername());
        // System.err.println("auth : " + authentication.getPrincipal());
        String accessToken = this.authService.generateAccessToken(user.getUsername());
        String refreshToken = this.authService.generateRefreshToken(user.getUsername());

        Cookie refreshTokenCookie = new Cookie("refreshToken",refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/api/auth/refresh");
        refreshTokenCookie.setMaxAge(7 * 60 * 60 * 24);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(new JwtResponse(accessToken));

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().body("Logout successful");
    }



    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@CookieValue(value="refreshToken", required=false) String refreshToken) {
        if (refreshToken == null || !jwtTokenUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = jwtTokenUtil.getUsernameFromToken(refreshToken);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, null,new ArrayList<>()));

        String newAccessToken = jwtTokenUtil.generateAccessToken(authentication.getPrincipal().toString());

        return ResponseEntity.ok(new JwtResponse(newAccessToken));

    }



    @PostMapping("/oauth2")
    public ResponseEntity<JwtResponseGoogle> authenticateOAuth(@RequestBody TokenRequest tokenRequest) {
        try {
            // Initialize the verifier
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY)
                    .setAudience(Collections.singletonList(clientId)) // Replace with your actual client ID
                    .build();


            // Validate the token
            GoogleIdToken idToken = verifier.verify(tokenRequest.getToken() ); // Use getToken instead of getTokenServerUrl
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Extract user information
                String userId = payload.getSubject();
                String email = payload.getEmail();
                boolean emailVerified = payload.getEmailVerified();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");
                Long expiration = (Long) payload.get("iat");
                // Log user information (for debugging only; remove in production)
                System.out.printf("Google User Info: userId=%s, email=%s, name=%s, pictureUrl=%s%n",
                        userId, email, name, pictureUrl);

                this.userService.createGoogleUser(name,email,pictureUrl,userId);

                // Example response: Only include necessary user details
                JwtResponseGoogle jwtResponse = new JwtResponseGoogle(expiration, email, name, pictureUrl, userId);

                return ResponseEntity.ok(jwtResponse);
            } else {
                // Invalid token
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new JwtResponseGoogle(-1L));
            }
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // Return an error response
            JwtResponseGoogle errorResponse = new JwtResponseGoogle(-2L);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}


