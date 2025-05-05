package com.thomas.gestPro.service;

import com.google.api.client.googleapis.GoogleUtils;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.thomas.gestPro.Security.JwtResponse;
import com.thomas.gestPro.Security.JwtTokenUtil;
import com.thomas.gestPro.Security.TokenRequest;
import com.thomas.gestPro.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;

@Service
public class AuthService {


    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtUtil;
    private final UserService userService;
    private final TemporaryUserService temporaryUserService;
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtTokenUtil jwtUtil, UserService userService, TemporaryUserService temporaryUserService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.temporaryUserService = temporaryUserService;
    }

    private String generateAccessToken(String username) {

        return this.jwtUtil.generateAccessToken(username);
    }
    private String generateRefreshToken(String username) {
        return this.jwtUtil.generateRefreshToken(username);
    }
    private ResponseCookie createJwtCookie(String tokenName, String tokenValue) {
        long maxAgeSeconds = tokenName.equals("accessToken") ?
                Duration.ofMinutes(15).getSeconds() :
                Duration.ofDays(7).getSeconds();

        return ResponseCookie.from(tokenName,tokenValue)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(maxAgeSeconds)
                .build();
    }

    private ResponseEntity<JwtResponse> validateJwtGoogle(String token) throws GeneralSecurityException, IOException {
        // Validate the token
        String clientId = "392803604648-dk6fp063ihhicrvpaqd3m95l4hbe26p2.apps.googleusercontent.com";
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY)
                .setAudience(Collections.singletonList(clientId)) // Replace with your actual client ID
                .build();

        GoogleIdToken idToken = verifier.verify(token); // Use getToken instead of getTokenServerUrl
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            // Extract user information
            String userId = payload.getSubject();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");

            ResponseCookie accessTokenCookie = this.createJwtCookie("accessTokenId",clientId);
            User googleUser = this.userService.createGoogleUser(name, email, pictureUrl, userId);
            return this.generateTokenAndCreateCookie(name,googleUser);
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new JwtResponse("false"));
        }
    }
    private ResponseEntity<JwtResponse> generateTokenAndCreateCookie(String username,User user) {
        String accessToken = this.generateAccessToken(username);
        String refreshToken = this.generateRefreshToken(username);

        ResponseCookie accessTokenCookie = this.createJwtCookie("accessToken",accessToken);
        ResponseCookie revokeTokenCookie = this.createJwtCookie("refreshToken",refreshToken);
        System.err.println("accessToken: " + accessTokenCookie);
        return ResponseEntity.ok()
                .header("Set-Cookie",accessTokenCookie.toString())
                .header("Set-Cookie",revokeTokenCookie.toString())
                .body(new JwtResponse(user));
    }

    public ResponseEntity<JwtResponse> authenticate(User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        System.err.println("Authorities : " + authentication.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User currentUser = this.userService.getUserByUsername(user.getUsername());

        return this.generateTokenAndCreateCookie(user.getUsername(),currentUser);

    }

//    public ResponseEntity<User> getCurrentUser() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = (String) authentication.getPrincipal();
//        User user = this.userService.getUserByUsername(username);
//        System.err.println(username);
//        return authentication.isAuthenticated() ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
//    }
    public ResponseEntity<JwtResponse> logout() {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          authentication.setAuthenticated(false);
//       // Supprimer immédiatement le cookie
         ResponseCookie revokeTokenCookie = this.jwtUtil.deleteJwtCookie("refreshToken");
         ResponseCookie accessTokenCookie = this.jwtUtil.deleteJwtCookie("accessToken");
         ResponseCookie githubTokenCookie = this.jwtUtil.deleteJwtCookie("gh_token");

        return ResponseEntity.ok()
                 .header("Set-Cookie", accessTokenCookie.toString())
                 .header("Set-Cookie",revokeTokenCookie.toString())
                .header("Set-Cookie",githubTokenCookie.toString())
                .body(null);

    }

    public ResponseEntity<JwtResponse> refresh(String refreshToken) {

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = this.jwtUtil.getUsernameFromToken(refreshToken);
        UserDetails userDetails = this.temporaryUserService.loadUserByUsername(username);
        boolean isValid = this.jwtUtil.validateToken(refreshToken,userDetails);

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User currentUser = this.userService.getUserByUsername(username);
        return ResponseEntity.ok()
                .body(new JwtResponse(currentUser));

    }

    public ResponseEntity<JwtResponse> authenticateOAuth(TokenRequest tokenRequest) {
        try {
            return this.validateJwtGoogle(tokenRequest.getToken());
        } catch (GeneralSecurityException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new JwtResponse(e.getMessage()));
        }
    }

    public ResponseEntity<JwtResponse> getCurrentUser( String token, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                //System.err.println(cookie.getName());
                if (cookie.getName().equals("gh_token")) {
                    return ResponseEntity.ok(new JwtResponse(cookie.getValue()));
                }
            }
        }
        if (this.jwtUtil.validateGoogleToken(token)) {
            return ResponseEntity.ok(new JwtResponse(this.jwtUtil.getUsernameFromToken(token)));
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.err.println("authentication  : " + authentication);
        if (authentication != null &&
                authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken)) {

            User user = userService.getUserByUsername(authentication.getName());
            return ResponseEntity.ok(new JwtResponse(user));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}