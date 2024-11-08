package com.thomas.gestPro.controller;

import com.thomas.gestPro.model.Card;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.model.Workspace;
import com.thomas.gestPro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
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

    /*@GetMapping("/protected-endpoint")
    public ResponseEntity<?> getProtectedData(HttpServletRequest request) {
        // Récupérer l'en-tête Authorization
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Extraire le token après "Bearer "
            // Vous pouvez maintenant utiliser et valider le token JWT
            return ResponseEntity.ok(new JwtResponse(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header is missing or invalid");
        }
    }*/
   @GetMapping("/username/{name}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String name) {
       return ResponseEntity.ok(userService.getUserByUsername(name));
   }

    @GetMapping("{id}/workspaces")
    public ResponseEntity<List<Workspace>> getWorkspaceByUserId(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.err.println(authentication);
        if (authentication != null && authentication.isAuthenticated()) {
            boolean hasUserRole = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"));

            if (hasUserRole) {
                // L'utilisateur a le rôle ADMIN
                System.out.println("L'utilisateur a le rôle USER");
            } else {
                // L'utilisateur n'a pas le rôle ADMIN
                System.err.println(authentication);
                System.out.println("L'utilisateur n'a pas le rôle USER");
            }
        }
        return ResponseEntity.ok(userService.getWorkspacesByUserId(id));
    }

    @PostMapping("/{id}/addCard")
    public ResponseEntity<User> addCardToUser(@PathVariable Long id, @RequestBody Card card) {
       User updatUser = userService.addCardToUser(id,card.getId());
        return ResponseEntity.ok(updatUser);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<User> updateUserById(@PathVariable Long id, @RequestBody User user) {
        User updateUser = userService.updateUser(id, user);
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
