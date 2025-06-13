package com.thomas.gestPro.service.integration;

import com.thomas.gestPro.dto.WorkspaceDTO;
import com.thomas.gestPro.mapper.BoardMapper;
import com.thomas.gestPro.mapper.UserMapper;
import com.thomas.gestPro.mapper.WorkspaceMapper;
import com.thomas.gestPro.model.Board;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.model.Workspace;
import com.thomas.gestPro.repository.BoardRepository;
import com.thomas.gestPro.repository.UserRepository;
import com.thomas.gestPro.repository.WorkspaceRepository;
import com.thomas.gestPro.service.WorkspaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class WorkspaceServiceIntegrationTest {

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private WorkspaceMapper workspaceMapper;

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private UserMapper userMapper;

    private User testUser;
    private Workspace testWorkspace;
    private Board testBoard;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new User();
        testUser.setEmail("test@test.com");
        testUser.setUsername("testUser");
        testUser.setPassword("password");
        userRepository.save(testUser);

        // Create test workspace
        testWorkspace = new Workspace();
        testWorkspace.setName("Test Workspace");
        testWorkspace.setDescription("Test Description");
        testWorkspace.setIsFavorite(false);
        testWorkspace.setUpdateAt(new Date());
        testWorkspace.getUsers().add(testUser);
        workspaceRepository.save(testWorkspace);

        // Create test board
        testBoard = new Board();
        testBoard.setName("Test Board");
        testBoard.getWorkspaces().add(testWorkspace);
        boardRepository.save(testBoard);
    }

    @Test
    void getAllWorkspaces_ShouldReturnAllWorkspaces() {
        List<Workspace> workspaces = workspaceService.getAllWorkspaces();
        assertFalse(workspaces.isEmpty());
        assertTrue(workspaces.contains(testWorkspace));
    }

    @Test
    void getWorkspaceById_ShouldReturnWorkspace() {
        WorkspaceDTO workspace = workspaceService.getWorkspaceById(testWorkspace.getId());
        assertNotNull(workspace);
        assertEquals(testWorkspace.getName(), workspace.getName());
    }

    @Test
    void createWorkspace_ShouldCreateNewWorkspace() {
        Workspace newWorkspace = new Workspace();
        newWorkspace.setName("New Workspace");

        WorkspaceDTO created = workspaceService.createWorkspace(testUser.getId(), newWorkspace);

        assertNotNull(created);
        assertEquals(newWorkspace.getName(), created.getName());
    }

    @Test
    void getWorkspacesByUserId_ShouldReturnUserWorkspaces() {
        List<WorkspaceDTO> workspaces = workspaceService.getWorkspacesByUserId(testUser.getId());
        assertFalse(workspaces.isEmpty());
        assertTrue(workspaces.stream().anyMatch(w -> w.getName().equals(testWorkspace.getName())));
    }

    @Test
    void updateWorkspace_ShouldUpdateWorkspace() {
        testWorkspace.setName("Updated Name");
        testWorkspace.setDescription("Updated Description");

        WorkspaceDTO updated = workspaceService.updateWorkspace(testWorkspace.getId(), testWorkspace);

        assertNotNull(updated);
        assertEquals("Updated Name", updated.getName());
        assertEquals("Updated Description", updated.getDescription());
    }

    @Test
    void deleteWorkspace_ShouldDeleteWorkspace() {
        workspaceService.deleteWorkspace(testWorkspace.getId());
        assertFalse(workspaceRepository.existsById(testWorkspace.getId()));
    }

    @Test
    void getWorkspacesByBoardId_ShouldReturnWorkspace() {
        WorkspaceDTO workspace = workspaceService.getWorkspacesByBoardId(testBoard.getId());
        assertNotNull(workspace);
        assertEquals(testWorkspace.getName(), workspace.getName());
    }
}
