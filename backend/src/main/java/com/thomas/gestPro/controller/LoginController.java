package com.thomas.gestPro.controller;

import com.thomas.gestPro.model.Users;
import com.thomas.gestPro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@CrossOrigin(value = "http://localhost:4200", allowCredentials = "true")
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{email}")
    public ResponseEntity<Users> getLogin(@PathVariable String email) {
        Users existingUser = userService.getUserByEmail(email);
        return  ResponseEntity.ok(existingUser);
    }




}

