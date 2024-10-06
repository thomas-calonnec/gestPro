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
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {
    private final UsersRepository userRepository;
    private final CardRepository cardRepository;
    private final WorkspaceRepository workspaceRepository;

    @Autowired
    public UserService(UsersRepository userRepository, CardRepository cardRepository, WorkspaceRepository workspaceRepository) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.workspaceRepository = workspaceRepository;
    }

    public Users getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public List<Users> getAllUser() {
        return userRepository.findAll();
    }

    public void createUser(Users user) {
        Users userExist = userRepository.findByUserName(user.getUserName());
        if (userExist != null) {
            throw new InvalidInputException("User already exists");
        }
        userRepository.save(user);
    }

    public Users updateUser(Long userId, Users updateUser){
       Users existingUser = getUserById(userId);

        existingUser.setUserName(updateUser.getUserName());
        existingUser.setUserEmail(updateUser.getUserEmail());
        existingUser.setUserPassword(updateUser.getUserPassword());

        return userRepository.save(existingUser);

    }
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public void addCardToUser(Long userId, Long cardId) {
        Users user = getUserById(userId);
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new ResourceNotFoundException("Card not found"));

        user.getCards().add(card);
        userRepository.save(user);

        card.getUsers().add(user);
        cardRepository.save(card);
    }

    @Transactional
    public Workspace createWorkspace(Long userId, Workspace workspace) {
        Users existingUser = getUserById(userId);

        workspace.getUsers().add(existingUser);
        workspaceRepository.save(workspace);
        existingUser.getWorkspaces().add(workspace);
        userRepository.save(existingUser);
        return workspace;
    }
}
