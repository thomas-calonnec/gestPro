package com.thomas.gestPro.controller;

import com.thomas.gestPro.dto.BoardDTO;
import com.thomas.gestPro.dto.WorkspaceDTO;
import com.thomas.gestPro.model.Workspace;
import com.thomas.gestPro.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("{id}/workspaces")
    public ResponseEntity<List<WorkspaceDTO>> getWorkspaceByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(workspaceService.getWorkspacesByUserId(id));
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<WorkspaceDTO> getWorkspaceByBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(workspaceService.getWorkspacesByBoardId(boardId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceDTO> getWorkspaceById(@PathVariable Long id) {
        WorkspaceDTO workspace = workspaceService.getWorkspaceById(id);
        return ResponseEntity.ok(workspace);
    }

    @PostMapping("/{id}/workspace")
    public ResponseEntity<WorkspaceDTO> createWorkspace( @PathVariable Long id, @RequestBody Workspace workspace) {
        WorkspaceDTO newWorkspace = workspaceService.createWorkspace(id,workspace);
        return ResponseEntity.ok(newWorkspace);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<WorkspaceDTO> updateWorkspaceById(@PathVariable Long id, @RequestBody Workspace workspace) {
        WorkspaceDTO updateWorkspace = workspaceService.updateWorkspace(id,workspace);
        return ResponseEntity.ok(updateWorkspace);
    }

    @DeleteMapping("{id}/delete")
    public void deleteWorkspaceById(@PathVariable Long id) {
        workspaceService.deleteWorkspace(id);
    }

}
