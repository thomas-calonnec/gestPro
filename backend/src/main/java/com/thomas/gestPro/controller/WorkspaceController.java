package com.thomas.gestPro.controller;

import com.thomas.gestPro.model.Board;
import com.thomas.gestPro.model.Workspace;
import com.thomas.gestPro.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/workspaces")
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
    public ResponseEntity<Workspace> getWorkspaceById(@PathVariable Long id) {
        Workspace workspace = workspaceService.getWorkspaceById(id);
        return ResponseEntity.ok(workspace);
    }

    @GetMapping("/listWorkspace/{userId}")
    public ResponseEntity<Set<Workspace>> getWorkspaceByUserId(@PathVariable Long userId) {
            return ResponseEntity.ok(workspaceService.getListWorkspaceByUserId(userId));
    }

    @GetMapping("{id}/listBoard")
    public ResponseEntity<Set<Board>> getListBoardByWorkspaceId(@PathVariable Long id) {
        Set<Board> boards = workspaceService.getListBoardByWorkspaceId(id);
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
