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
import com.thomas.gestPro.service.LoginService;
import com.thomas.gestPro.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
   private final LoginService loginService;
    private final AuthenticationManager authenticationManager;
   private JwtTokenUtil jwtTokenUtil;
   private final UserDetailsService userDetailsService;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Autowired
    public AuthController(UserService userService, LoginService loginService, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.loginService = loginService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody User user, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("user : " + user.getUsername());
       // System.err.println("auth : " + authentication.getPrincipal());
        String accessToken = this.loginService.generateAccessToken(user.getUsername());
        String refreshToken = this.loginService.generateRefreshToken(user.getUsername());

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken",accessToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(3600)
                .build();

        ResponseCookie revokeTokenCookie = ResponseCookie.from("revokeToken",refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(3600)
                .build();
        User currentUser = this.userService.getUserByUsername(user.getUsername());
        return ResponseEntity.ok()
                .header("Set-Cookie",accessTokenCookie.toString())
                .header("Set-Cookie",revokeTokenCookie.toString())
                .body(new JwtResponse(currentUser));

        //return ResponseEntity.status(HttpStatus.FOUND).body();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        authentication.setAuthenticated(false);
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken","")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build(); // Supprimer immédiatement le cookie
        ResponseCookie revokeTokenCookie = ResponseCookie.from("revokeToken","")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();


        return ResponseEntity.ok()
                .header("Set-Cookie", accessTokenCookie.toString())
                .header("Set-Cookie",revokeTokenCookie.toString())
                .body(null);
    }



    @PostMapping("/revoke-token")
    public ResponseEntity<?> refreshAccessToken(@CookieValue(value="revokeToken", required=false) String refreshToken) {
        if (refreshToken == null || !jwtTokenUtil.validateToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = jwtTokenUtil.getUsernameFromToken(refreshToken);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, null,new ArrayList<>()));


        User currentUser = this.userService.getUserByUsername(username);


        return ResponseEntity.ok()
                .body(new JwtResponse(currentUser));

    }

    @PostMapping("/oauth2")
    public ResponseEntity<JwtResponse> authenticateOAuth(@RequestBody TokenRequest tokenRequest,HttpServletResponse response) {
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

               User googleUser = this.userService.createGoogleUser(name,email,pictureUrl,userId);

                ResponseCookie refreshTokenCookie = ResponseCookie.from("accessToken",tokenRequest.getToken())
                .httpOnly(true)
                        .secure(true)
                        .sameSite("Strict")
                        .path("/")
                        .maxAge(3600)
                        .build(); // Supprimer immédiatement le cookie
                // Example response: Only include necessary user details
               // JwtResponseGoogle jwtResponseGoogle = new JwtResponseGoogle(expiration, email, name, pictureUrl, userId);
              //  JwtResponse jwtResponse = new JwtResponse(tokenRequest.getToken());
                return ResponseEntity.ok()
                        .header("Set-Cookie",refreshTokenCookie.toString())
                        .body(new JwtResponse(googleUser));
            } else {
                // Invalid token
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new JwtResponse(false));
            }
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // Return an error response
            String errorResponse =  "error : " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JwtResponse(false));
        }

    }
}



