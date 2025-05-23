package com.thomas.gestPro.controller;

import com.thomas.gestPro.dto.BoardDTO;
import com.thomas.gestPro.dto.WorkspaceDTO;
import com.thomas.gestPro.model.Board;
import com.thomas.gestPro.model.Workspace;
import com.thomas.gestPro.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/user/workspaces")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @Autowired
    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @GetMapping
    public ResponseEntity<List<Workspace>> getAllWorkspaces() {
        return ResponseEntity.ok(workspaceService.getAllWorkspaces());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceDTO> getWorkspaceById(@PathVariable Long id) {
        WorkspaceDTO workspace = workspaceService.getWorkspaceById(id);
        return ResponseEntity.ok(workspace);
    }


    @GetMapping("{id}/boards")
    public ResponseEntity<List<BoardDTO>> getListBoardByWorkspaceId(@PathVariable Long id) {
        List<BoardDTO> boards = workspaceService.getListBoardByWorkspaceId(id);

        return new ResponseEntity<>(boards, HttpStatus.OK);
    }

    @PostMapping("{id}/board")
    public ResponseEntity<Board> createBoard(@PathVariable Long id, @RequestBody Board board) {
        Board newBoard = workspaceService.createBoard(id,board);
        return new ResponseEntity<>(newBoard, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Workspace> updateWorkspaceById(@PathVariable Long id, @RequestBody Workspace workspace) {
        Workspace updateWorkspace = workspaceService.updateWorkspace(id,workspace);
        return ResponseEntity.ok(updateWorkspace);
    }

    @DeleteMapping("{id}/delete")
    public void deleteWorkspaceById(@PathVariable Long id) {
        workspaceService.deleteWorkspace(id);
    }

}
