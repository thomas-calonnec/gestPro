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
import java.util.List;

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
     * Retrieves the list of workspaces associated with a specific user.
     *
     * @param userId the user's identifier
     * @return a list of Workspaces associated with the user
     * @throws RuntimeException if the user with the specified ID does not exist
     */
    public List<Workspace> getListWorkspaceByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getWorkspaces();
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

        workspace.getBoards().add(board);
        boardRepository.save(board);
        workspaceRepository.save(workspace);

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
