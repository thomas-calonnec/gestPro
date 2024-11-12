package com.thomas.gestPro.Security;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.lang.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;  // Classe utilitaire pour générer/valider les JWT

    public JwtRequestFilter(UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable FilterChain chain)
            throws ServletException, IOException {

        // Récupérer le token d'accès depuis l'en-tête Authorization

        String authHeader = request.getHeader("Authorization");
        System.err.println("username :" + authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7); // Extraire le token après "Bearer "

            try {
                // Vérifier si le token est valide
                if (jwtTokenUtil.validateToken(accessToken)) {

                    String username = jwtTokenUtil.getUsernameFromToken(accessToken);

                    if (username != null) {
                        // Authentifier l'utilisateur
                        // Vous pouvez créer un objet d'authentification avec le nom d'utilisateur et l'ajouter au contexte de sécurité
                        JwtAuthenticationToken authentication = new JwtAuthenticationToken(accessToken, username);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } else {
                    // Si le token d'accès est expiré, essayer de renouveler le token avec le refresh token
                    String refreshToken = jwtTokenUtil.getRefreshTokenFromRequest(request);
                    if (refreshToken != null && jwtTokenUtil.validateToken(refreshToken)) {
                        String newAccessToken = jwtTokenUtil.refreshAccessToken(refreshToken);
                        // Mettre à jour l'en-tête Authorization avec le nouveau token
                        assert response != null;
                        response.setHeader("Authorization", "Bearer " + newAccessToken);
                    }
                }
            } catch (ExpiredJwtException e) {
                // Gestion de l'expiration du token
                logger.error("Token expiré", e);
            } catch (JwtException e) {
                // Gestion des erreurs de décodage du token
                logger.error("Erreur lors de l'analyse du token JWT", e);
            }
        }

        assert chain != null;
        chain.doFilter(request, response); // Continuer avec le filtre suivant
    }
}