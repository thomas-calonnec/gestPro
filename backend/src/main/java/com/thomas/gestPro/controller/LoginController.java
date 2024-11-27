package com.thomas.gestPro.controller;

import com.thomas.gestPro.Security.JwtResponse;
import com.thomas.gestPro.Security.JwtTokenUtil;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.service.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class LoginController {

   private final LoginService loginService;
    private final AuthenticationManager authenticationManager;
   private JwtTokenUtil jwtTokenUtil;
   private final UserDetailsService userDetailsService;

    @Autowired
    public LoginController(LoginService loginService, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsService) {
        this.loginService = loginService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody User user, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("user : " + user.getUsername());
       // System.err.println("auth : " + authentication.getPrincipal());
        String accessToken = this.loginService.generateAccessToken(user.getUsername());
        String refreshToken = this.loginService.generateRefreshToken(user.getUsername());

        Cookie refreshTokenCookie = new Cookie("refreshToken",refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/api/auth/refresh");
        refreshTokenCookie.setMaxAge(7 * 60 * 60 * 24);
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok(new JwtResponse(accessToken));

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().body("Logout successful");
    }



    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@CookieValue(value="refreshToken", required=false) String refreshToken) {
        if (refreshToken == null || !jwtTokenUtil.validateToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = jwtTokenUtil.getUsernameFromToken(refreshToken);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, null,new ArrayList<>()));

        String newAccessToken = jwtTokenUtil.generateAccessToken(authentication.getPrincipal().toString());

        return ResponseEntity.ok(new JwtResponse(newAccessToken));

    }
}



