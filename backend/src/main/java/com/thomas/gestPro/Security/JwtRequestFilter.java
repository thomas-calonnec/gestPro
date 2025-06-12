package com.thomas.gestPro.Security;


import com.thomas.gestPro.service.TemporaryUserService;
import io.micrometer.common.lang.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtUtil;
    private final TemporaryUserService temporaryUserService;
    public JwtRequestFilter(JwtTokenUtil jwtUtil, TemporaryUserService temporaryUserService) {
        // Classe utilitaire pour générer/valider les JWT
        this.jwtUtil = jwtUtil;
        this.temporaryUserService = temporaryUserService;

    }
    @Override
    protected void doFilterInternal(@Nullable HttpServletRequest request,
                                    @Nullable HttpServletResponse response,
                                   @Nullable FilterChain filterChain) throws ServletException, IOException {

        String token = null;

        // Extrait le token depuis un cookie HttpOnly
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                System.out.println(cookie.getName());
                if ("accessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        if (token != null) {
            String username = jwtUtil.getUsernameFromToken(token);
            UserDetails userDetails = temporaryUserService.loadUserByUsername(username);

            if(jwtUtil.validateToken(token,userDetails)) {
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        }

        filterChain.doFilter(request, response);
        }
}