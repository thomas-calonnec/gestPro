package com.thomas.gestPro.service.integration;

import com.thomas.gestPro.dto.BoardDTO;
import com.thomas.gestPro.dto.UserDTO;
import com.thomas.gestPro.dto.UserLightDTO;
import com.thomas.gestPro.model.Board;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.model.Workspace;
import com.thomas.gestPro.service.BoardService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BoardServiceIntegrationTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private EntityManager entityManager;

    @Test
    void testCreateBoard() {
        Workspace workspace = Workspace.builder()
                .name("Test Workspace")
                .description("Test Workspace Description")
                .build();
        entityManager.persist(workspace);
        entityManager.flush();

        BoardDTO boardInput = new BoardDTO();
        boardInput.setName("Test Board");
        boardInput.setDescription("Test Description");
        boardInput.setColor("#000000");
        boardInput.setLastUpdated(new Date());
        boardInput.setCardCount(0);
        boardInput.setStatus("active");
        boardInput.setMembers(new ArrayList<>(List.of()));

        BoardDTO savedBoard = boardService.createBoard(workspace.getId(), boardInput);

        assertNotNull(savedBoard.getId());
        assertEquals("Test Board", savedBoard.getName());
    }

    @Test
    void testGetBoardById() {
        Workspace workspace = Workspace.builder()
                .name("Test Workspace")
                .description("Test Workspace Description")
                .build();
        entityManager.persist(workspace);

        Board board = Board.builder()
                .name("Test Board")
                .description("Test Description")
                .color("#000000")
                .lastUpdated(new Date())
                .cardCount(0)
                .status("active")
                .build();
        board.getWorkspaces().add(workspace);

        entityManager.persist(board);
        entityManager.flush();

        BoardDTO foundBoard = boardService.getBoardById(board.getId());
        assertNotNull(foundBoard);
        assertEquals(board.getId(), foundBoard.getId());
    }

    //
    @Test
    void testUpdateBoard() {
        User user = new User();
        user.setEmail("toto.calonnec@gmail.com");
        user.setUsername("root");
        entityManager.persist(user);

        Workspace workspace = Workspace.builder()
                .name("Test Workspace")
                .description("Test Workspace Description")
                .build();
        entityManager.persist(workspace);

        Board board = Board.builder()
                .name("Test Board")
                .description("Test Description")
                .color("#000000")
                .lastUpdated(new Date())
                .cardCount(0)
                .status("active")
                .build();
        board.getWorkspaces().add(workspace);

        entityManager.persist(board);
        entityManager.flush();

        BoardDTO updateDTO = new BoardDTO();
        updateDTO.setName("Updated Board");
        updateDTO.setDescription("Updated Description");
        updateDTO.setStatus("inactive");
        updateDTO.setOwnerId(board.getOwnerId());
        updateDTO.setMembers(new ArrayList<>(List.of(UserLightDTO
                .builder()
                .id(1L)
                .email("toto.calonnec@gmail.com")
                .username("root")
                .build())));
        BoardDTO updatedBoard = boardService.updateBoard(board.getId(), updateDTO);

        assertEquals("Updated Board", updatedBoard.getName());
        assertEquals("Updated Description", updatedBoard.getDescription());
        assertEquals("inactive", updatedBoard.getStatus());
    }

    @Test
    void testDeleteBoard() {
        Workspace workspace = Workspace.builder()
                .name("Test Workspace")
                .description("Test Workspace Description")
                .build();
        entityManager.persist(workspace);

        Board board = Board.builder()
                .name("Test Board")
                .description("Test Description")
                .color("#000000")
                .lastUpdated(new Date())
                .cardCount(0)
                .status("active")
                .build();
        board.getWorkspaces().add(workspace);

        entityManager.persist(board);
        entityManager.flush();

        Long boardId = board.getId();
        boardService.deleteBoard(boardId);

        assertNull(entityManager.find(Board.class, boardId));
    }

}
