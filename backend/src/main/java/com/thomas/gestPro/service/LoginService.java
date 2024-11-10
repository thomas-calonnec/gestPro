package com.thomas.gestPro.service;

import com.thomas.gestPro.Security.JwtResponse;
import com.thomas.gestPro.Security.JwtTokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final TemporaryUserService userDetailsService;

    public LoginService(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, TemporaryUserService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    public JwtResponse login(String username, String password) {
        // Authentifier l'utilisateur via le AuthenticationManager de Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        // Si l'authentification réussit, charger les détails de l'utilisateur
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Générer le token JWT à partir des détails de l'utilisateur
         String accessToken = jwtTokenUtil.generateAccessToken(userDetails);
         String refreshToken = jwtTokenUtil.generateRefreshToken(accessToken);

         return new JwtResponse(accessToken, refreshToken);
    }
}