package com.thomas.gestPro.controller;

import com.thomas.gestPro.model.Card;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.model.Workspace;
import com.thomas.gestPro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(value = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("{id}/workspaces")
    public ResponseEntity<Set<Workspace>> getWorkspaceByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getWorkspacesByUserId(id));
    }

    @GetMapping("/listUser")
    public ResponseEntity<List<User>> getListOfUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PostMapping("/{id}/addCard")
    public ResponseEntity<User> addCardToUser(@PathVariable Long id, @RequestBody Card card) {
       User updatUser = userService.addCardToUser(id,card.getId());
        return ResponseEntity.ok(updatUser);
    }

    @PutMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        userService.createUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<User> updateUserById(@PathVariable Long id, @RequestBody User user) {
        User updateUser = userService.updateUser(id,user);
        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/workspace")
    public ResponseEntity<Workspace> createWorkspace( @PathVariable Long id, @RequestBody Workspace workspace) {
        Workspace newWorkspace = userService.createWorkspace(id,workspace);
        return ResponseEntity.ok(newWorkspace);
    }



    /*@PostMapping("/login")
    public ResponseEntity<User> setLogin(@RequestBody User user) {

        User existingUser = userService.getUserByEmail(email);
        return  ResponseEntity.ok(existingUser);
    }*/
  /*
  @PostMapping("/deleteCard/{id}")
    public ResponseEntity<String> removeLabelFromUser(@PathVariable Long id, @RequestBody Label label) {
        userService.removeLabelFromUser(id,label.getLabelId());
        return ResponseEntity.ok("User deleted");
    }*/
    
}
