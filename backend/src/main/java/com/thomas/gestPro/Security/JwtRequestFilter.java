package com.thomas.gestPro.Security;


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

        final String authorizationHeader = request.getHeader("Authorization");
        System.err.println("auth : " + authorizationHeader);

        String username = null;
        String jwtAccessToken = null;
        String jwtRefreshToken;

        // Récupérer tous les en-têtes pour le débogage
       /* StringBuilder headersInfo = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headersInfo.append(headerName).append(": ").append(headerValue).append("\n");
        }*/
        // System.err.println("All headers: " + headersInfo);

        // Extraction du token d'accès
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtAccessToken = authorizationHeader.substring(7);
            username = jwtTokenUtil.extractUsername(jwtAccessToken);
        }

        // Vérifiez si l'utilisateur est authentifié dans le contexte de sécurité
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.isTokenExpired(jwtAccessToken)) {
                // Si le token d'accès est expiré, essayez d'utiliser le token de rafraîchissement
                jwtRefreshToken = request.getHeader("Authorization"); // ou depuis les cookies, selon votre implémentation

                if (jwtRefreshToken != null && jwtTokenUtil.validateToken(jwtRefreshToken, userDetails)) {
                    // Générer un nouveau token d'accès
                    String newAccessToken = jwtTokenUtil.generateAccessToken(userDetails);

                    // Ajouter le nouveau token d'accès dans l'en-tête de réponse
                    if (response != null) {
                        response.setHeader("Authorization", "Bearer " + newAccessToken);
                    }

                    // Créer un nouveau `UsernamePasswordAuthenticationToken` pour ce nouvel accès
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    // Le token de rafraîchissement est invalide ou expiré : déconnexion nécessaire
                    if (response != null) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Le token de rafraîchissement est expiré ou invalide");
                    }
                    return; // Fin du filtre sans continuer
                }
            } else if (jwtTokenUtil.validateToken(jwtAccessToken, userDetails)) {
                // Si le token d'accès est valide, continuez normalement
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        // Continuer avec le filtre
        assert chain != null;
        chain.doFilter(request, response);
    }

}