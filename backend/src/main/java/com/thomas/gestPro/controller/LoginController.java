package com.thomas.gestPro.controller;

import com.thomas.gestPro.Security.JwtResponse;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.service.LoginService;
import com.thomas.gestPro.service.TemporaryUserService;
import com.thomas.gestPro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loginForm")
@CrossOrigin(origins = "http://192.168.1.138:4200", allowCredentials = "true")
public class LoginController {

   private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;

    }

    @PostMapping
    public ResponseEntity<?> getLogin(@RequestBody User user) {


        try {
            // Appeler le service pour authentifier l'utilisateur et générer le JWT
            String token = loginService.login(user.getUsername(), user.getPassword());

            // Retourner le token au client
            return ResponseEntity.ok(new JwtResponse(token));

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

    }

}



