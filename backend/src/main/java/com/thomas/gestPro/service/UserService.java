package com.thomas.gestPro.service;

import com.thomas.gestPro.Exception.InvalidInputException;
import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.dto.UserDTO;
import com.thomas.gestPro.dto.WorkspaceDTO;
import com.thomas.gestPro.mapper.UserMapper;
import com.thomas.gestPro.mapper.WorkspaceMapper;
import com.thomas.gestPro.model.Role;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.repository.CardRepository;
import com.thomas.gestPro.repository.RoleRepository;
import com.thomas.gestPro.repository.UserRepository;
import com.thomas.gestPro.repository.WorkspaceRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
public class UserService {
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final RoleRepository roleRepository;

    /**
     * Constructor with dependency injection for repositories.
     * s
     * 
     * @param userRepository      repository for managing users
     * @param cardRepository      repository for managing cards
     * @param workspaceRepository repository for managing workspaces
     */
    @Autowired
    public UserService(UserRepository userRepository, CardRepository cardRepository,
            WorkspaceRepository workspaceRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.workspaceRepository = workspaceRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the found user
     * @throws ResourceNotFoundException if the user is not found
     */
    public UserDTO getById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return UserMapper.toDTO(user);
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
        return userRepository.findAll().stream().map(UserMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Creates a new user if the username does not already exist.
     *
     *
     * @throws InvalidInputException if a user with the same username already exists
     */
//    @Transactional
//    public User createUserGithub(User incomingUser) {
//        Optional<UserDTO> optionalUser = userRepository.findByEmail(incomingUser.getEmail());
//
//        Role userRole = roleRepository.findByName("ROLE_USER");
//
//        if (optionalUser.isEmpty()) {
//            // Création
//            UserDTO newUser = new User();
//            newUser.setEmail(incomingUser.getEmail());
//            newUser.setUsername(incomingUser.getUsername());
//
//           // newUser.setRoles(new ArrayList<>(List.of(userRole)));
//            userRepository.save(newUser);
//            return newUser;
//        }
//        return optionalUser.get();
//    }

    /**
     * Updates an existing user's details.
     *
     * @param userId     the ID of the user to update
     * @param updateUser the user object containing updated information
     * @return the updated user
     */
    // public User updateUser(Long userId, User updateUser) {
    //     User existingUser = getById(userId);
    //     if (existingUser.isPresent()) {
    //         User user = existingUser.get();
    //         user.setUsername(updateUser.getUsername());
    //         user.setEmail(updateUser.getEmail());
    //         user.setPassword(updateUser.getPassword());
    //         user.setRoles(updateUser.getRoles());
    //         return userRepository.save(user);
    //     }

    //     return null;

    // }

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     */
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

//    /**
//     * Adds a card to a user by their respective IDs.
//     *
//     * @param userId the ID of the user
//     * @param cardId the ID of the card
//     * @throws ResourceNotFoundException if the card is not found
//     */
    // public User addCardToUser(Long userId, Long cardId) {
    //     Optional<User> existedUser = getById(userId);
    //     Card card = cardRepository.findById(cardId).orElseThrow(() -> new ResourceNotFoundException("Card not found"));

    //     if (existedUser.isPresent()) {
    //         User user = existedUser.get();
    //         user.getCards().add(card);
    //         userRepository.save(user);

    //         card.getUsers().add(user);
    //         cardRepository.save(card);

    //         return user;
    //     }

    //     return null;
    // }

//    /**
//     * Creates a new workspace and adds it to a user's list of workspaces.
//     *
//     * @param userId    the ID of the user who will own the workspace
//     * @param workspace the workspace to create
//     * @return the created workspace
//     */
    // @Transactional
    // public Workspace createWorkspace(Long userId, Workspace workspace) {
    //     Optional<User> existingUser = getById(userId);

    //     if (existingUser.isPresent()) {
    //         User user = existingUser.get();

    //         Workspace newWorkspace = new Workspace();
    //         newWorkspace.setName(workspace.getName());
    //         newWorkspace.getUsers().add(user);

    //         workspaceRepository.save(newWorkspace);

    //         user.getWorkspaces().add(newWorkspace);

    //         userRepository.save(user);
    //         return newWorkspace;
    //     }
    //     return null;
    // }

    public UserDTO getUserByUsername(String username) {
        return userRepository.getUserByUsername(username)
                .map(UserMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
    }

    // public List<Workspace> getWorkspacesByUserId(Long userId) {
    //     Optional<User> existingUser = getById(userId);
    //     if (existingUser.isPresent()) {
    //         User user = existingUser.get();
    //         return user.getWorkspaces();
    //     }
    //     return null;

    // }

    public List<WorkspaceDTO> getWorkspacesByUserId(Long userId) {
        return userRepository.findById(userId)
        .map(user -> user.getWorkspaces().stream()
            .map(WorkspaceMapper::toDTO)
            .collect(Collectors.toList())
        )
        .orElse(Collections.emptyList());
       
    }

    public UserDTO createGoogleUser(String username, String email, String pictureUrl, String googleId) {

        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return UserMapper.toDTO(user.get());
        }

        User googleUser = new User();
        googleUser.setEmail(email);
        googleUser.setPictureUrl(pictureUrl);
        googleUser.setUsername(username);

        Role userRole = roleRepository.findByName("ROLE_USER");
        googleUser.getRoles().add(userRole);

        userRepository.save(googleUser);

        return UserMapper.toDTO(googleUser);
    }


}
