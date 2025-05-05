package com.thomas.gestPro.controller;

import com.thomas.gestPro.model.User;
import com.thomas.gestPro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/listUser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getListOfUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }



}
