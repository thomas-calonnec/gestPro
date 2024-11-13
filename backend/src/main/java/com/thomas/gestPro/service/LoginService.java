package com.thomas.gestPro.service;

import com.thomas.gestPro.Security.JwtResponse;
import com.thomas.gestPro.Security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class LoginService {


    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtUtil;

    private final UserDetailsService userDetailsService;

    @Autowired
    public LoginService(AuthenticationManager authenticationManager, JwtTokenUtil jwtUtil, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

        this.userDetailsService = userDetailsService;
    }

    public JwtResponse login(String username, String password) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // Check if authentication was successful
            if (authentication.isAuthenticated()) {
                // Set authentication context
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Load user details and generate tokens
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                String accessToken = jwtUtil.generateAccessToken(userDetails);
                String refreshToken = jwtUtil.generateRefreshToken(username);

                // Return the JWT tokens in the response
                return new JwtResponse(accessToken, refreshToken);
            } else {
                throw new RuntimeException("Invalid username or password");
            }
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
        }
    }

    // Method to refresh the access token using the refresh token
    public String refreshAccessToken(String refreshToken) {
        String username = jwtUtil.getUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        // Validate refresh token (check expiration, signature, etc.)
        if (jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh token expired");
        }
        return "Bearer " + jwtUtil.generateAccessToken(userDetails);
    }
}