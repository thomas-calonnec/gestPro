package com.thomas.gestPro.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public String generateAccessToken(UserDetails userDetails) {

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
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

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate JWT token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    // Extraire le Refresh Token d'une requête
    public String getRefreshTokenFromRequest(HttpServletRequest request) {
        return request.getHeader("Refresh-Token");
    }

    // Rafraîchir le token d'accès avec un refresh token
    public String refreshAccessToken(String refreshToken) {
        // Logique pour valider le refresh token et créer un nouveau access token
        String username = getUsernameFromToken(refreshToken); // Par exemple, obtenir le nom d'utilisateur à partir du refresh token
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return generateAccessToken(userDetails); // Générer un nouveau access token
    }
}
