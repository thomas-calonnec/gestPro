package com.thomas.gestPro.Security;


import io.micrometer.common.lang.NonNullApi;
import io.micrometer.common.lang.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;  // Classe utilitaire pour générer/valider les JWT
   private final UserDetailsService userDetailsService;

    public JwtRequestFilter(JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearToken = request.getHeader("Authorization");
        if (bearToken != null && bearToken.startsWith("Bearer ")) {
            return bearToken.substring(7); // Extracts the token part after "Bearer "
        }
        return null;
    }
    @Override
    protected void doFilterInternal(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable FilterChain chain)
            throws ServletException, IOException {

//        assert request != null;
//        String token = extractJwtFromRequest(request);
//
//        if (token != null && jwtTokenUtil.validateToken(token)) {
//            String username = jwtTokenUtil.getUsernameFromToken(token);
//
//            System.err.println("username : " +username);
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//        assert chain != null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                System.err.println("Cookie: " + cookie.getName());
                if ("accessToken".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (jwtTokenUtil.validateToken(token)) {
                        String username = jwtTokenUtil.getUsernameFromToken(token);
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }


    // Récupérer le token d'accès depuis l'en-tête Authorization
//
//    String authHeader = request.getHeader("Authorization");
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//        String accessToken = authHeader.substring(7); // Extraire le token après "Bearer "
//
//        try {
//            // Vérifier si le token est valide
//            if (jwtTokenUtil.validateToken(accessToken)) {
//
//                String username = jwtTokenUtil.getUsernameFromToken(accessToken);
//
//                if (username != null) {
//                    // Authentifier l'utilisateur
//                    // Vous pouvez créer un objet d'authentification avec le nom d'utilisateur et l'ajouter au contexte de sécurité
//                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//
//                    var authToken = new UsernamePasswordAuthenticationToken(
//                            userDetails, userDetails.getAuthorities());
//                    //authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                    System.err.println("Authentication set: " + SecurityContextHolder.getContext().getAuthentication());
//                }
//            } else {
//                // Si le token d'accès est expiré, essayer de renouveler le token avec le refresh token
//                String refreshToken = jwtTokenUtil.getRefreshTokenFromRequest(request);
//                if (refreshToken != null && jwtTokenUtil.validateToken(refreshToken)) {
//                    String newAccessToken = jwtTokenUtil.refreshAccessToken(refreshToken);
//                    // Mettre à jour l'en-tête Authorization avec le nouveau token
//                    assert response != null;
//                    response.setHeader("Authorization", "Bearer " + newAccessToken);
//                }
//            }
//        } catch (ExpiredJwtException e) {
//            // Gestion de l'expiration du token
//            logger.error("Token expiré", e);
//        } catch (JwtException e) {
//            // Gestion des erreurs de décodage du token
//            logger.error("Erreur lors de l'analyse du token JWT", e);
//        }
//    }
//
//        assert chain != null;
//        chain.doFilter(request, response); // Continuer avec le filtre suivant
//}
}