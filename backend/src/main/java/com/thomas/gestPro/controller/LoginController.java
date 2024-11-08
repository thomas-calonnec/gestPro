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

@RestController
@RequestMapping("/loginForm")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class LoginController {

   private final LoginService loginService;
 private final UserDetailsService userDetailsService;
    @Autowired
    public LoginController(LoginService loginService, UserDetailsService userDetailsService) {
        this.loginService = loginService;

        this.userDetailsService = userDetailsService;
    }

    @PostMapping
    public ResponseEntity<?> getLogin(@RequestBody User user) {
        JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
        try {
            // Appeler le service pour authentifier l'utilisateur et générer le JWT
            String token = loginService.login(user.getUsername(), user.getPassword());

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(user.getUsername());


            // Retourner le token au client
            return ResponseEntity.ok(new JwtResponse(token));

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok().body("Logout successful");
    }

}



