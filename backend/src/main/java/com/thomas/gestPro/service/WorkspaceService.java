package com.thomas.gestPro.service;


import com.thomas.gestPro.dto.BoardDTO;
import com.thomas.gestPro.dto.WorkspaceDTO;
import com.thomas.gestPro.mapper.BoardMapper;
import com.thomas.gestPro.mapper.UserMapper;
import com.thomas.gestPro.mapper.WorkspaceMapper;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.model.Workspace;
import com.thomas.gestPro.repository.BoardRepository;
import com.thomas.gestPro.repository.UserRepository;
import com.thomas.gestPro.repository.WorkspaceRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    private final WorkspaceMapper workspaceMapper;
    private final BoardMapper boardMapper;
    private final UserMapper userMapper;

    /**
     * Constructor to inject the necessary repositories.
     *
     * @param workspaceRepository the repository for workspace management
     * @param usersRepository the repository for user management
     * @param boardRepository the repository for board management
     */
    @Autowired
    public WorkspaceService(WorkspaceRepository workspaceRepository, UserRepository usersRepository, BoardRepository boardRepository, WorkspaceMapper workspaceMapper, BoardMapper boardMapper, UserMapper userMapper) {
        this.workspaceRepository = workspaceRepository;
        this.userRepository = usersRepository;
        this.boardRepository = boardRepository;
        this.workspaceMapper = workspaceMapper;
        this.boardMapper = boardMapper;
        this.userMapper = userMapper;
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
     * Récupère un Workspace par son ID.
     *
     * @param workspaceId l'identifiant du Workspace
     * @return le Workspace correspondant
     * @throws RuntimeException si le Workspace avec l'ID spécifié n'existe pas
     */
    public WorkspaceDTO getWorkspaceById(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new RuntimeException("Workspace not found"));
        return workspaceMapper.toDTO(workspace);
    }

    /**
     * Creates a new workspace and adds it to a user's list of workspaces.
     *
     * @param userId    the ID of the user who will own the workspace
     * @param workspace the workspace to create
     * @return the created workspace
     */
    @Transactional
    public WorkspaceDTO createWorkspace(Long userId, Workspace workspace) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Workspace newWorkspace = new Workspace();
        newWorkspace.setName(workspace.getName());
        newWorkspace.getUsers().add(user);

        workspaceRepository.save(newWorkspace);
        user.getWorkspaces().add(newWorkspace);

        userRepository.save(user);
        return workspaceMapper.toDTO(newWorkspace);

    }

    public List<WorkspaceDTO> getWorkspacesByUserId(Long userId) {

        return userRepository.findById(userId)
                .map(user -> user.getWorkspaces().stream()
                        .map(workspaceMapper::toDTO)
                        .collect(Collectors.toList())
                )
                .orElse(Collections.emptyList());

    }

    /**
     * Updates a Workspace with new information.
     *
     * @param id the identifier of the Workspace to be updated
     * @param updateWorkspace the Workspace containing the new information
     * @return the updated Workspace
     * @throws RuntimeException if the Workspace with the specified ID does not exist.
     */
    public WorkspaceDTO updateWorkspace(Long id, Workspace updateWorkspace){

        workspaceRepository.findById(id)
                .map(workspace -> {
                    workspace.setName(updateWorkspace.getName());
                    workspace.setDescription(updateWorkspace.getDescription());
                    workspace.setUpdateAt(updateWorkspace.getUpdateAt());
                    workspace.setIsFavorite(updateWorkspace.getIsFavorite());
                    return workspaceRepository.save(workspace);
                })
                .orElseThrow(() -> new RuntimeException("Workspace not found"));
        return workspaceMapper.toDTO(updateWorkspace);
    }

    /**
     * Deletes a Workspace by its ID.
     *
     * @param workspaceId the identifier of the workspace to delete
     */
    public void deleteWorkspace(Long workspaceId){
        workspaceRepository.deleteById(workspaceId);
    }

    public WorkspaceDTO getWorkspacesByBoardId(Long boardId) {
        return this.workspaceRepository
                .findAll()
                .stream()
                .filter(workspace ->
                        workspace.getBoards().stream().anyMatch(board -> board.getId().equals(boardId))
                )
                .findFirst()
                .map(workspaceMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

    }
}
