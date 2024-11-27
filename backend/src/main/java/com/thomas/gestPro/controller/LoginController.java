package com.thomas.gestPro.controller;

import com.thomas.gestPro.Security.JwtResponse;
import com.thomas.gestPro.Security.JwtTokenUtil;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/loginForm")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class LoginController {

   private final LoginService loginService;
   private JwtTokenUtil jwtTokenUtil;
   private final UserDetailsService userDetailsService;

    @Autowired
    public LoginController(LoginService loginService, UserDetailsService userDetailsService) {
        this.loginService = loginService;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> getLogin(@RequestBody User user) {

        try {

            // Appeler le service pour authentifier l'utilisateur et générer le JWT
            JwtResponse token = loginService.login(user.getUsername(), user.getPassword());
            

            // Retourner le token au client
            return ResponseEntity.ok(token);

        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }

    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().body("Logout successful");
    }



    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken) {

        if (!jwtTokenUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(403).body("Invalid or expired refresh token");
        }

        String username = jwtTokenUtil.extractUsername(refreshToken);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        String newAccessToken = jwtTokenUtil.generateAccessToken(userDetails);

        return ResponseEntity.ok(newAccessToken);
    }
}



