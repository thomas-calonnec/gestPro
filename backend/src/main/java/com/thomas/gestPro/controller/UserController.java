package com.thomas.gestPro.controller;

import com.thomas.gestPro.dto.UserDTO;
import com.thomas.gestPro.dto.WorkspaceDTO;
import com.thomas.gestPro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/user/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

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


    @GetMapping("{id}/workspaces")
    public ResponseEntity<List<WorkspaceDTO>> getWorkspaceByUserId(@PathVariable Long id) {

        for(WorkspaceDTO workspaceDTOList : userService.getWorkspacesByUserId(id) ) {
            System.err.println(workspaceDTOList.getId());
        }
        return ResponseEntity.ok(userService.getWorkspacesByUserId(id));
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
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

//    @PostMapping("/{id}/workspace")
//    public ResponseEntity<WorkspaceDTO> createWorkspace( @PathVariable Long id, @RequestBody Workspace workspace) {
//        Workspace newWorkspace = userService.createWorkspace(id,workspace);
//        return ResponseEntity.ok(newWorkspace);
//    }
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
