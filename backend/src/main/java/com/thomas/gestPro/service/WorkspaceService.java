package com.thomas.gestPro.service;


import com.thomas.gestPro.model.Board;
import com.thomas.gestPro.model.Users;
import com.thomas.gestPro.model.Workspace;
import com.thomas.gestPro.repository.BoardRepository;
import com.thomas.gestPro.repository.UsersRepository;
import com.thomas.gestPro.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Data
@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UsersRepository usersRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public WorkspaceService(WorkspaceRepository workspaceRepository, UsersRepository usersRepository, BoardRepository boardRepository) {
        this.workspaceRepository = workspaceRepository;
        this.usersRepository = usersRepository;
        this.boardRepository = boardRepository;
    }

    public Set<Board> getListBoardByWorkspaceId(Long workspaceId) {
       Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new RuntimeException("Workspace not found"));
       return workspace.getBoards();

    }

    public Set<Workspace> getListWorkspaceByUserId(Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getWorkspaces();
    }

    public Workspace getWorkspaceById(Long workspaceId) {
        return workspaceRepository.findById(workspaceId).orElseThrow(() -> new RuntimeException("Workspace not found"));
    }


    @Transactional
    public Board createBoard(Long workspaceId, Board board) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new RuntimeException("Workspace not found"));

        workspace.getBoards().add(board);
        boardRepository.save(board);
        workspaceRepository.save(workspace);

        return board;

    }

    public Workspace updateWorkspace(Long id, Workspace updateWorkspace){
        workspaceRepository.findById(id)
                .map(workspace -> {
                    workspace.setWorkspaceName(updateWorkspace.getWorkspaceName());
                    workspace.setWorkspaceDescription(updateWorkspace.getWorkspaceDescription());
                    return workspaceRepository.save(workspace);
                })
                .orElseThrow(() -> new RuntimeException("Workspace not found"));
        return updateWorkspace;
    }

    public void deleteWorkspace(Long workspaceId){
        workspaceRepository.deleteById(workspaceId);
    }
}
