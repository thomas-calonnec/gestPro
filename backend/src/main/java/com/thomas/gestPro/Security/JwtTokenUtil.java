package com.thomas.gestPro.Security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;


@Service
public class JwtTokenUtil {


    private static final String SECRET_KEY = "L8p#R9m$K2x^V7j!Q3w*F5z@T6y&N0d$Y4u"; // Clé secrète, peut être externalisée
    private final Key secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes()); // Clé correcte avec taille appropriée
    private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 60; // 1 hours
    private static final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24 * 7; // 7 jours
    private final UserDetailsService userDetailsService;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();


    @Autowired
    public JwtTokenUtil(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    // Générer un Access Token
    public String generateAccessToken(String username) {

        return Jwts.builder()
                .setSubject(username)
                .signWith(secretKey,SignatureAlgorithm.HS256)
                .claim("roles","USER")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .compact();
    }

    // Générer un Refresh Token
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(secretKey,SignatureAlgorithm.HS256 )
                .claim("roles","USER")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .compact();
    }

    public boolean validateGoogleToken(String token) {
        try{
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY)
                    .setAudience(Collections.singletonList(clientId)) // Replace with your actual client ID
                    .build();


            // Validate the token
            GoogleIdToken idToken = verifier.verify(token);

            if(idToken != null) {
                return true;
            }
        } catch (Exception e ) {
            e.printStackTrace();
            return false;
        }
       return false;
    }
    // Valider un token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Extraire le nom d'utilisateur d'un token
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    // Extract username from JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date from JWT token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract claims from token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }



}
