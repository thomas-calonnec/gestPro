package com.thomas.gestPro.service;

import com.thomas.gestPro.Security.JwtResponse;
import com.thomas.gestPro.Security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class LoginService {


    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtUtil;

    @Autowired
    public LoginService(AuthenticationManager authenticationManager, JwtTokenUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

    }

    public JwtResponse login(String username, String password) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        if (authentication.isAuthenticated()) {
            // Generate and return access and refresh tokens
            String accessToken = jwtUtil.generateAccessToken(username);
            String refreshToken = jwtUtil.generateRefreshToken(username);
            // Store refresh token in database or cache if needed for future use
            return new JwtResponse(accessToken,refreshToken);  // Return the JWT Token
        }
        throw new RuntimeException("Invalid username or password");
    }

    // Method to refresh the access token using the refresh token
    public String refreshAccessToken(String refreshToken) {
        String username = jwtUtil.getUsernameFromToken(refreshToken);
        // Validate refresh token (check expiration, signature, etc.)
        if (jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh token expired");
        }
        return "Bearer " + jwtUtil.generateAccessToken(username);
    }
}