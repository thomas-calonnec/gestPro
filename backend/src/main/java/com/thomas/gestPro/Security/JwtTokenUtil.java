package com.thomas.gestPro.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenUtil {


    private static final String SECRET_KEY = "L8p#R9m$K2x^V7j!Q3w*F5z@T6y&N0d$Y4u"; // Clé secrète, peut être externalisée
    private final Key secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes()); // Clé correcte avec taille appropriée
    private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 60; // 1 hours
    private static final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 6; // 6 heures
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtTokenUtil(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Générer un Access Token
    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(secretKey,SignatureAlgorithm.HS256)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
                .compact();
    }

    // Générer un Refresh Token
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(secretKey,SignatureAlgorithm.HS256 )
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .compact();
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

    // Extraire le Refresh Token d'une requête
    public String getRefreshTokenFromRequest(HttpServletRequest request) {
        return request.getHeader("Refresh-Token");
    }

    // Rafraîchir le token d'accès avec un refresh token
    public String refreshAccessToken(String refreshToken) {
        // Logique pour valider le refresh token et créer un nouveau access token
        String username = getUsernameFromToken(refreshToken); // Par exemple, obtenir le nom d'utilisateur à partir du refresh token
        return generateAccessToken(username); // Générer un nouveau access token
    }
}
