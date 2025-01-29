package com.thomas.gestPro.service;


import com.thomas.gestPro.model.Board;
import com.thomas.gestPro.model.Workspace;
import com.thomas.gestPro.repository.BoardRepository;
import com.thomas.gestPro.repository.UserRepository;
import com.thomas.gestPro.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

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
     * @param board the Board object to be added
     * @return the newly created Board
     * @throws RuntimeException if the Workspace with the specified ID does not exist.
     */
    @Transactional
    public Board createBoard(Long workspaceId, Board board) {

        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new RuntimeException("Workspace not found"));
        List<String> colors = List.of(
                "red", "blue", "green", "yellow", "purple",
                "orange", "pink", "brown", "mediumaquamarine", "cyan"
        );

        // Generate a random index
        Random random = new Random();
        int randomIndex = random.nextInt(colors.size());

        // Pick one random color
        String randomColor = colors.get(randomIndex);

        // Print the random color
        System.err.println("Random Color: " + randomColor);
        board.setColor(randomColor);
        board.setLastUpdated(new Date());
        board.setCardCount(0);
        workspace.getBoards().add(board);
        workspaceRepository.save(workspace);
        try {
            boardRepository.save(board);
        } catch (ObjectOptimisticLockingFailureException e) {
            // Recharger l'entité et réessayer
            System.err.println("err" + e.getMessage());
            Board reloadedBoard = boardRepository.findById(board.getId()).orElseThrow(() -> new RuntimeException("Board not found"));
            // Appliquer les modifications à l'entité récupérée
            boardRepository.save(reloadedBoard);
        }

        return board;

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
        workspaceRepository.findById(id)
                .map(workspace -> {
                    workspace.setName(updateWorkspace.getName());
                    workspace.setDescription(updateWorkspace.getDescription());
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
