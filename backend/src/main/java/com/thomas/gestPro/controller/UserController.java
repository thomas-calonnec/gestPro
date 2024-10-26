package com.thomas.gestPro.controller;

import com.thomas.gestPro.model.Card;
import com.thomas.gestPro.model.Users;
import com.thomas.gestPro.model.Workspace;
import com.thomas.gestPro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUsersById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("{id}/workspaces")
    public ResponseEntity<Set<Workspace>> getWorkspaceByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getWorkspacesByUserId(id));
    }

    @GetMapping("/listUsers")
    public ResponseEntity<List<Users>> getListOfUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PostMapping("/{id}/addCard")
    public ResponseEntity<Users> addCardToUser(@PathVariable Long id, @RequestBody Card card) {
       Users updatUsers = userService.addCardToUser(id,card.getCardId());
        return ResponseEntity.ok(updatUsers);
    }

    @PutMapping("/create")
    public ResponseEntity<Users> createUser(@RequestBody Users user) {
        userService.createUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Users> updateUsersById(@PathVariable Long id, @RequestBody Users user) {
        Users updateUser = userService.updateUser(id,user);
        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteUsersById(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/workspace")
    public ResponseEntity<Workspace> createWorkspace( @PathVariable Long id, @RequestBody Workspace workspace) {
        Workspace newWorkspace = userService.createWorkspace(id,workspace);
        return ResponseEntity.ok(newWorkspace);
    }

    @GetMapping("/login/{email}")
    
    public ResponseEntity<Users> getLogin(@PathVariable String email) {
        Users existingUser = userService.getUserByEmail(email);
        return  ResponseEntity.ok(existingUser);
    }

    @PostMapping("/login")

    public ResponseEntity<Users> setLogin(@RequestBody Users user) {
        String email;
        Users existingUser = userService.getUserByEmail(email);
        return  ResponseEntity.ok(existingUser);
    }
  /*
  @PostMapping("/deleteCard/{id}")
    public ResponseEntity<String> removeLabelFromUsers(@PathVariable Long id, @RequestBody Label label) {
        userService.removeLabelFromUser(id,label.getLabelId());
        return ResponseEntity.ok("Users deleted");
    }*/
    
}
