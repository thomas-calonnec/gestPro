package com.thomas.gestPro.controller;

import com.thomas.gestPro.model.User;
import com.thomas.gestPro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loginForm")
@CrossOrigin(origins = "http://192.168.1.138:4200", allowCredentials = "true")
public class LoginController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public LoginController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<?> getLogin(@RequestBody User users) {


        User existingUser = userService.getByUsername(users.getUsername());

        if (existingUser != null) {
            System.err.println("Mot de passe fourni : " + users.getPassword());
            System.err.println("Mot de passe stocké : " + existingUser.getPassword());

            // Comparez le mot de passe fourni avec le mot de passe existant
            if (users.getPassword().equals(existingUser.getPassword())) {
                return ResponseEntity.ok(existingUser   ); // Authentification réussie
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mot de passe incorrect"); // Authentification échouée
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utilisateur non trouvé"); // Utilisateur non trouvé
    }




}

