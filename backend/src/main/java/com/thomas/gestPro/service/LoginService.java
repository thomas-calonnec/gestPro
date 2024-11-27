package com.thomas.gestPro.service;

import com.thomas.gestPro.Security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

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

    public String generateAccessToken(String username) {

        return this.jwtUtil.generateAccessToken(username);
    }
    public String generateRefreshToken(String username) {
        return this.jwtUtil.generateRefreshToken(username);
    }

//    public JwtResponse login(String username, String password) {
//        try {
//            // Authenticate the user
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(username, password)
//            );
//
//            // Check if authentication was successful
//            if (authentication.isAuthenticated()) {
//                // Set authentication context
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//
//                // Load user details and generate tokens
//                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//                String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername());
//                String refreshToken = jwtUtil.generateRefreshToken(username);
//
//                // Return the JWT tokens in the response
//                return new JwtResponse(accessToken, refreshToken);
//            } else {
//                throw new RuntimeException("Invalid username or password");
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
//        }
//    }

    // Method to refresh the access token using the refresh token

}