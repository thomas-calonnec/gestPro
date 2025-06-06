package com.thomas.gestPro.controller;

import com.thomas.gestPro.dto.UserDTO;
import com.thomas.gestPro.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("/username/{name}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String name) {
        return ResponseEntity.ok(userService.getUserByUsername(name));
    }


//    @PostMapping("/{id}/addCard")
//    public ResponseEntity<UserDTO> addCardToUser(@PathVariable Long id, @RequestBody Card card) {
//        User updatUser = userService.addCardToUser(id,card.getId());
//        return ResponseEntity.ok(updatUser);
//    }
//
//    @PutMapping("/{id}/update")
//    public ResponseEntity<UserDTO> updateUserById(@PathVariable Long id, @RequestBody User user) {
//        User updateUser = userService.updateUser(id, user);
//        return ResponseEntity.ok(updateUser);
//    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


//
//    @PutMapping("/create")
//    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
//        User newUser = userService.createUserGithub(user);
//        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
//    }


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
