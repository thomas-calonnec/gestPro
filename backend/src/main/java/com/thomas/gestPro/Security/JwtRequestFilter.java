package com.thomas.gestPro.Security;


import io.micrometer.common.lang.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    public JwtRequestFilter() {
        // Classe utilitaire pour générer/valider les JWT
    }
    @Override
    protected void doFilterInternal(@Nullable HttpServletRequest request,
                                    @Nullable HttpServletResponse response,
                                   @Nullable FilterChain filterChain) throws ServletException, IOException {


        assert filterChain != null;
        filterChain.doFilter(request, response); // Passe à la requête suivante
        }
}