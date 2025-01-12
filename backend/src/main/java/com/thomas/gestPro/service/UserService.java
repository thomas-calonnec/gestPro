package com.thomas.gestPro.service;

import com.thomas.gestPro.Exception.InvalidInputException;
import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.model.Card;
import com.thomas.gestPro.model.Role;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.model.Workspace;
import com.thomas.gestPro.repository.CardRepository;
import com.thomas.gestPro.repository.RoleRepository;
import com.thomas.gestPro.repository.UserRepository;
import com.thomas.gestPro.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing user-related operations.
 * This class provides methods for retrieving, creating, updating, and deleting user information.
 * It also manages user associations with cards and workspaces.
 *
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    /**
     * Constructor with dependency injection for repositories.
     *s
     * @param userRepository repository for managing users
     * @param cardRepository repository for managing cards
     * @param workspaceRepository repository for managing workspaces
     */
    @Autowired
    public UserService(UserRepository userRepository, CardRepository cardRepository, WorkspaceRepository workspaceRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.workspaceRepository = workspaceRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the found user
     * @throws ResourceNotFoundException if the user is not found
     */
    public User getById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

   /* public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }*/

    /**
     * Retrieves a list of all users.
     *
     * @return a list of all users
     */
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    /**
     * Creates a new user if the username does not already exist.
     *
     * @param user the user to create
     * @throws InvalidInputException if a user with the same username already exists
     */
    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_USER");

        user.getRoles().add(userRole);

         userRepository.save(user);

    }

    /**
     * Updates an existing user's details.
     *
     * @param userId the ID of the user to update
     * @param updateUser the user object containing updated information
     * @return the updated user
     */
    public User updateUser(Long userId, User updateUser){
       User existingUser = getById(userId);

        existingUser.setUsername(updateUser.getUsername());
        existingUser.setEmail(updateUser.getEmail());
        existingUser.setPassword(updateUser.getPassword());
        existingUser.setRoles(updateUser.getRoles());

        return userRepository.save(existingUser);

    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     */
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }


    /**
     * Adds a card to a user by their respective IDs.
     *
     * @param userId the ID of the user
     * @param cardId the ID of the card
     * @throws ResourceNotFoundException if the card is not found
     */
    public User addCardToUser(Long userId, Long cardId) {
        User user = getById(userId);
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new ResourceNotFoundException("Card not found"));

        user.getCards().add(card);
        userRepository.save(user);

        card.getUsers().add(user);
        cardRepository.save(card);

        return user;
    }

    /**
     * Creates a new workspace and adds it to a user's list of workspaces.
     *
     * @param userId the ID of the user who will own the workspace
     * @param workspace the workspace to create
     * @return the created workspace
     */
    @Transactional
    public Workspace createWorkspace(Long userId, Workspace workspace) {
        User existingUser = getById(userId);

        Workspace newWorkspace = new Workspace();
        newWorkspace.setName(workspace.getName());
        newWorkspace.getUsers().add(existingUser);

        workspaceRepository.save(newWorkspace);

        existingUser.getWorkspaces().add(newWorkspace);

        userRepository.save(existingUser);
        return newWorkspace;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);

    }

    public List<Workspace> getWorkspacesByUserId(Long userId) {
        return this.getById(userId).getWorkspaces();
    }


}
