package com.thomas.gestPro.controller;

import com.thomas.gestPro.Security.JwtResponse;
import com.thomas.gestPro.Security.JwtTokenUtil;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loginForm")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class LoginController {

   private final LoginService loginService;


    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
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
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
        // Vérifier si le refresh token est valide

        try {
            // Appeler le service pour authentifier l'utilisateur et générer le JWT
            String newAccessToken = this.loginService.refreshAccessToken(refreshToken);


            // Retourner le token au client
            return ResponseEntity.ok(newAccessToken);

        } catch (Exception e) {
            return ResponseEntity.status(401).body(null);
        }
       //


    }
}



