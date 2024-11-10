package com.thomas.gestPro.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
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
    private static final long ACCESS_TOKEN_VALIDITY = 1000 * 60 * 60; // 15 minutes
    private static final long REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24; // 24 heures

    // Génère un token d'accès avec des claims supplémentaires
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());
        return createToken(claims, userDetails.getUsername(), ACCESS_TOKEN_VALIDITY);
    }

    // Génère un token de rafraîchissement sans claims spécifiques
    public String generateRefreshToken(String username) {
        return createToken(new HashMap<>(), username, REFRESH_TOKEN_VALIDITY);
    }

    // Méthode générique de création de token
    private String createToken(Map<String, Object> claims, String subject, long validity) {
        long currentTimeMillis = System.currentTimeMillis();

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis + validity))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extrait le nom d'utilisateur du token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrait la date d'expiration du token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extraction générique de claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Récupère tous les claims du token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .setAllowedClockSkewSeconds(60) // Tolérance de 60 secondes
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Vérifie si le token est expiré
    protected Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Valide le token par rapport aux détails de l'utilisateur
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
