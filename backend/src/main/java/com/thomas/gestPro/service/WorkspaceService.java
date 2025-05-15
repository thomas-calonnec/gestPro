package com.thomas.gestPro.service;


import com.thomas.gestPro.model.Board;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.model.Workspace;
import com.thomas.gestPro.repository.BoardRepository;
import com.thomas.gestPro.repository.UserRepository;
import com.thomas.gestPro.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Workspace management service.
 * Allows you to manage Workspaces, Boards associated with these spaces,
 * and user relations.
 */
@Data
@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    /**
     * Constructor to inject the necessary repositories.
     *
     * @param workspaceRepository the repository for workspace management
     * @param usersRepository the repository for user management
     * @param boardRepository the repository for board management
     */
    @Autowired
    public WorkspaceService(WorkspaceRepository workspaceRepository, UserRepository usersRepository, BoardRepository boardRepository) {
        this.workspaceRepository = workspaceRepository;
        this.userRepository = usersRepository;
        this.boardRepository = boardRepository;
    }

    /**
     * Retrieves the list of all Workspaces.
     *
     * @return a list containing all Workspaces
     */
    public List<Workspace> getAllWorkspaces() {
        return workspaceRepository.findAll();
    }

    /**
     * Retrieves the list of Boards associated with a specific Workspace.
     *
     * @param workspaceId the Workspace identifier
     * @return a list of Boards associated with the Workspace
     * @throws RuntimeException if the Workspace with the specified ID does not exist.
     */
    public List<Board> getListBoardByWorkspaceId(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new RuntimeException("Workspace not found"));
        return workspace.getBoards();

    }

    /**
     * Récupère un Workspace par son ID.
     *
     * @param workspaceId l'identifiant du Workspace
     * @return le Workspace correspondant
     * @throws RuntimeException si le Workspace avec l'ID spécifié n'existe pas
     */
    public Workspace getWorkspaceById(Long workspaceId) {
        return workspaceRepository.findById(workspaceId).orElseThrow(() -> new RuntimeException("Workspace not found"));
    }

    /**
     * Creates a new Board in a given Workspace.
     *
     * @param workspaceId the identifier of the Workspace where the Board will be added.
     * @param boardInput the Board object to be added
     * @return the newly created Board
     * @throws RuntimeException if the Workspace with the specified ID does not exist.
     */
    @Transactional
    public Board createBoard(Long workspaceId, Board boardInput) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        // Charger les membres depuis la base pour éviter les entités détachées
        List<User> attachedMembers = boardInput.getMembers().stream()
                .map(user -> userRepository.findById(user.getId())
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + user.getId())))
                .toList();

        Board board = new Board();
        board.setName(boardInput.getName());
        board.setDescription(boardInput.getDescription());
        board.setColor(getRandomColor());
        board.setLastUpdated(new Date());
        board.setCardCount(0);
        board.setMembers(attachedMembers);
        board.setOwnerId(boardInput.getOwnerId());

        board.getWorkspaces().add(workspace);
        attachedMembers.forEach(user -> user.getBoards().add(board));
//        Optional<User> owner = this.userRepository.findById(board.getOwnerId());
//        owner.ifPresent(user -> user.getBoards().add(board));

        workspace.getBoards().add(board);

        boardRepository.save(board);
        return board;
    }

    private String getRandomColor() {
        List<String> colors = List.of(
                "red", "blue", "green", "yellow", "purple",
                "orange", "pink", "brown", "mediumaquamarine", "cyan"
        );
        return colors.get(new Random().nextInt(colors.size()));
    }

    /**
     * Updates a Workspace with new information.
     *
     * @param id the identifier of the Workspace to be updated
     * @param updateWorkspace the Workspace containing the new information
     * @return the updated Workspace
     * @throws RuntimeException if the Workspace with the specified ID does not exist.
     */
    public Workspace updateWorkspace(Long id, Workspace updateWorkspace){
        System.err.println("update : " + updateWorkspace.getIsFavorite());
        workspaceRepository.findById(id)
                .map(workspace -> {
                    workspace.setName(updateWorkspace.getName());
                    workspace.setDescription(updateWorkspace.getDescription());
                    workspace.setUpdateAt(updateWorkspace.getUpdateAt());
                    workspace.setIsFavorite(updateWorkspace.getIsFavorite());
                    return workspaceRepository.save(workspace);
                })
                .orElseThrow(() -> new RuntimeException("Workspace not found"));
        return updateWorkspace;
    }

    /**
     * Deletes a Workspace by its ID.
     *
     * @param workspaceId the identifier of the workspace to delete
     */
    public void deleteWorkspace(Long workspaceId){
        workspaceRepository.deleteById(workspaceId);
    }




}
