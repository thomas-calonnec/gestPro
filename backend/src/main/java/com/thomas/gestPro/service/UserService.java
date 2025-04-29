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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final RoleRepository roleRepository;

    /**
     * Constructor with dependency injection for repositories.
     *s
     * @param userRepository repository for managing users
     * @param cardRepository repository for managing cards
     * @param workspaceRepository repository for managing workspaces
     */
    @Autowired
    public UserService(UserRepository userRepository, CardRepository cardRepository, WorkspaceRepository workspaceRepository, RoleRepository roleRepository) {
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
    public Optional<User> getById(Long userId) {
        return userRepository.findById(userId) ;
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
     *
     * @throws InvalidInputException if a user with the same username already exists
     */
    @Transactional
    public void  createUserGithub(User incomingUser) {
        Optional<User> optionalUser = userRepository.findByProviderIdAndProviderName(
                incomingUser.getProviderId(),
                "Github"
                );

        Role userRole = roleRepository.findByName("ROLE_USER");

        if (optionalUser.isEmpty()) {
            // Création
            User user = new User();
            user.setProviderId(incomingUser.getProviderId());
            user.setProviderName("Github");
            user.setEmail(incomingUser.getEmail());
            user.setUsername(incomingUser.getUsername());

            user.setPassword(null); // ou une valeur par défaut
            user.setRoles(new ArrayList<>(List.of(userRole)));
            userRepository.save(user);
        }

    }

    /**
     * Updates an existing user's details.
     *
     * @param userId the ID of the user to update
     * @param updateUser the user object containing updated information
     * @return the updated user
     */
    public User updateUser(Long userId, User updateUser){
        Optional<User> existingUser = getById(userId);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setUsername(updateUser.getUsername());
            user.setEmail(updateUser.getEmail());
            user.setPassword(updateUser.getPassword());
            user.setRoles(updateUser.getRoles());
            return userRepository.save(user);
        }

        return null;

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
        Optional<User> existedUser = getById(userId);
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new ResourceNotFoundException("Card not found"));

        if(existedUser.isPresent()){
            User user = existedUser.get();
            user.getCards().add(card);
            userRepository.save(user);

            card.getUsers().add(user);
            cardRepository.save(card);

            return user;
        }

        return null;
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
        Optional<User> existingUser = getById(userId);

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            Workspace newWorkspace = new Workspace();
            newWorkspace.setName(workspace.getName());
            newWorkspace.getUsers().add(user);

            workspaceRepository.save(newWorkspace);

            user.getWorkspaces().add(newWorkspace);

            userRepository.save(user);
            return newWorkspace;
        }
        return null;
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername (username);

    }

    public Optional<User> getUserByProviderId(String providerId) {
        return userRepository.findByProviderId(providerId);
    }

    public List<Workspace> getWorkspacesByUserId(Long userId) {
        Optional<User> existingUser = getById(userId);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            return user.getWorkspaces();
        }
        return null;

    }

    public User createGoogleUser(String username, String email, String pictureUrl, String googleId) {

        if(userRepository.findByProviderId(googleId).isPresent()){
            Optional<User> optionalUser =  userRepository.findByProviderId(googleId);
            return optionalUser.orElse(null);

        }


        User googleUser = new User();
        googleUser.setEmail(email);
        googleUser.setPictureUrl(pictureUrl);
        googleUser.setUsername(username);
        googleUser.setProviderId(googleId);
        googleUser.setProviderName("google");

        Role userRole = roleRepository.findByName("ROLE_USER");
        googleUser.getRoles().add(userRole);

        userRepository.save(googleUser);

        return googleUser;
    }
}
