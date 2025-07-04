package com.thomas.gestPro.service;

import com.thomas.gestPro.Exception.InvalidInputException;
import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.dto.RegisterRequest;
import com.thomas.gestPro.dto.UserDTO;
import com.thomas.gestPro.mapper.UserMapper;
import com.thomas.gestPro.model.Role;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.repository.RoleRepository;
import com.thomas.gestPro.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing user-related operations.
 * This class provides methods for retrieving, creating, updating, and deleting
 * user information.
 * It also manages user associations with cards and workspaces.
 *
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;


    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the found user
     * @throws ResourceNotFoundException if the user is not found
     */
    public UserDTO getById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toDTO(user);
    }

    /*
     * public User getByUsername(String username) {
     * return userRepository.findByUsername(username);
     * }
     */

    /**
     * Retrieves a list of all users.
     *
     * @return a list of all users
     */
    public List<UserDTO> getAllUser() {
        return userRepository.findAll().stream().map(userMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Creates a new user if the username does not already exist.
     *
     *
     * @throws InvalidInputException if a user with the same username already exists
     */
    @Transactional
    public UserDTO createUserGithub(User incomingUser) {
        Optional<User> optionalUser = userRepository.findByEmail(incomingUser.getEmail());

//        Role userRole = roleRepository.findByName("ROLE_USER");

        if (optionalUser.isEmpty()) {
            // Création
            User newUser = new User();
            newUser.setEmail(incomingUser.getEmail());
            newUser.setUsername(incomingUser.getUsername());

           // newUser.setRoles(new ArrayList<>(List.of(userRole)));
            userRepository.save(newUser);
            return userMapper.toDTO(newUser);
        }
        return userMapper.toDTO(optionalUser.get());
    }

    /**
     * Updates an existing user's details.
     *
     * @param userId     the ID of the user to update
     * @param updateUser the user object containing updated information
     * @return the updated user
     */
     public UserDTO updateUser(Long userId, User updateUser) {
         UserDTO existingUser = getById(userId);

         User user = userMapper.toEntity(existingUser);
         user.setUsername(updateUser.getUsername());
         user.setEmail(updateUser.getEmail());
         user.setPassword(updateUser.getPassword());
         user.setRoles(updateUser.getRoles());
         userRepository.save(user);
         return userMapper.toDTO(user);
     }

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     */
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public UserDTO getUserByUsername(String username) {
        return userRepository.getUserByUsername(username)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    public UserDTO createGoogleUser(String username, String email, String pictureUrl, String googleId) {

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return userMapper.toDTO(user.get());
        }

        User googleUser = new User();
        googleUser.setEmail(email);
        googleUser.setPictureUrl(pictureUrl);
        googleUser.setUsername(username);

        Role userRole = roleRepository.findByName("ROLE_USER");
        googleUser.getRoles().add(userRole);

        userRepository.save(googleUser);

        return userMapper.toDTO(googleUser);
    }


    public boolean existsByUsernameOrEmail(String username, String email) {
        Optional<User> userByUsername = userRepository.getUserByUsername(username);
        Optional<User> userByEmail = userRepository.findByEmail(email);
        return userByUsername.isPresent() || userByEmail.isPresent();
    }

    public UserDTO createUser(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.getRoles().add(userRole);
        userRepository.save(user);
        return userMapper.toDTO(user);
    }
}
