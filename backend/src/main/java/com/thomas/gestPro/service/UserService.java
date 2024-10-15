package com.thomas.gestPro.service;

import com.thomas.gestPro.Exception.InvalidInputException;
import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.model.Card;
import com.thomas.gestPro.model.Users;
import com.thomas.gestPro.model.Workspace;
import com.thomas.gestPro.repository.CardRepository;
import com.thomas.gestPro.repository.UsersRepository;
import com.thomas.gestPro.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Service class for managing user-related operations.
 * This class provides methods for retrieving, creating, updating, and deleting user information.
 * It also manages user associations with cards and workspaces.
 *
 */
@Service
public class UserService {
    private final UsersRepository userRepository;
    private final CardRepository cardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor with dependency injection for repositories.
     *
     * @param userRepository repository for managing users
     * @param cardRepository repository for managing cards
     * @param workspaceRepository repository for managing workspaces
     */
    @Autowired
    public UserService(UsersRepository userRepository, CardRepository cardRepository, WorkspaceRepository workspaceRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.workspaceRepository = workspaceRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the found user
     * @throws ResourceNotFoundException if the user is not found
     */
    public Users getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    /**
     * Retrieves a list of all users.
     *
     * @return a list of all users
     */
    public List<Users> getAllUser() {
        return userRepository.findAll();
    }

    /**
     * Creates a new user if the username does not already exist.
     *
     * @param user the user to create
     * @throws InvalidInputException if a user with the same username already exists
     */
    public void createUser(Users user) {
        Users userExist = userRepository.findByUserName(user.getUserName());
        if (userExist != null) {
            throw new InvalidInputException("User already exists");
        }
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        userRepository.save(user);
    }

    /**
     * Updates an existing user's details.
     *
     * @param userId the ID of the user to update
     * @param updateUser the user object containing updated information
     * @return the updated user
     */
    public Users updateUser(Long userId, Users updateUser){
       Users existingUser = getUserById(userId);

        existingUser.setUserName(updateUser.getUserName());
        existingUser.setUserEmail(updateUser.getUserEmail());
        existingUser.setUserPassword(updateUser.getUserPassword());

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
    public Users addCardToUser(Long userId, Long cardId) {
        Users user = getUserById(userId);
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
        Users existingUser = getUserById(userId);

        workspace.getUsers().add(existingUser);
        workspaceRepository.save(workspace);
        existingUser.getWorkspaces().add(workspace);
        userRepository.save(existingUser);
        return workspace;
    }

    public Users getUserByEmail(String userEmail) {
        // TODO Auto-generated method stub
        return userRepository.getUserByUserEmail(userEmail);

    }

    public Set<Workspace> getWorkspacesByUserId(Long userId) {
        return this.getUserById(userId).getWorkspaces();
    }
}
