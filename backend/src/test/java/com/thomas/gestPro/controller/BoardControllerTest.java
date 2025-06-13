package com.thomas.gestPro.controller;

import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.dto.BoardDTO;
import com.thomas.gestPro.service.BoardService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
class BoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private BoardService mockBoardService;

    @Test
    void testGetBoardById() throws Exception {
        // Setup
        // Configure BoardService.getBoardById(...).
        final BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(0L);
        boardDTO.setName("name");
        boardDTO.setColor("color");
        boardDTO.setDescription("description");
        boardDTO.setLastUpdated(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        when(mockBoardService.getBoardById(0L)).thenReturn(boardDTO);

        // Run the test and verify the results
        mockMvc.perform(get("/api/user/boards/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testGetBoardById_BoardServiceThrowsResourceNotFoundException() throws Exception {
        // Setup
        when(mockBoardService.getBoardById(0L)).thenThrow(ResourceNotFoundException.class);

        // Run the test and verify the results
        mockMvc.perform(get("/api/user/boards/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testCreateBoard() throws Exception {
        // Setup
        // Configure BoardService.createBoard(...).
        final BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(0L);
        boardDTO.setName("name");
        boardDTO.setColor("color");
        boardDTO.setDescription("description");
        boardDTO.setLastUpdated(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        when(mockBoardService.createBoard(eq(0L), any(BoardDTO.class))).thenReturn(boardDTO);

        // Run the test and verify the results
        mockMvc.perform(post("/api/user/boards/{id}/board", 0)
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testCreateBoard_BoardServiceThrowsRuntimeException() throws Exception {
        // Setup
        when(mockBoardService.createBoard(eq(0L), any(BoardDTO.class))).thenThrow(RuntimeException.class);

        // Run the test and verify the results
        mockMvc.perform(post("/api/user/boards/{id}/board", 0)
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testGetListBoardByWorkspaceId() throws Exception {
        // Setup
        // Configure BoardService.getListBoardByWorkspaceId(...).
        final BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(0L);
        boardDTO.setName("name");
        boardDTO.setColor("color");
        boardDTO.setDescription("description");
        boardDTO.setLastUpdated(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        final List<BoardDTO> boardDTOS = List.of(boardDTO);
        when(mockBoardService.getListBoardByWorkspaceId(0L)).thenReturn(boardDTOS);

        // Run the test and verify the results
        mockMvc.perform(get("/api/user/boards/workspace/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testGetListBoardByWorkspaceId_BoardServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockBoardService.getListBoardByWorkspaceId(0L)).thenReturn(Collections.emptyList());

        // Run the test and verify the results
        mockMvc.perform(get("/api/user/boards/workspace/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]", true));
    }

    @Test
    void testGetListBoardByWorkspaceId_BoardServiceThrowsRuntimeException() throws Exception {
        // Setup
        when(mockBoardService.getListBoardByWorkspaceId(0L)).thenThrow(RuntimeException.class);

        // Run the test and verify the results
        mockMvc.perform(get("/api/user/boards/workspace/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testUpdateBoardById() throws Exception {
        // Setup
        // Configure BoardService.updateBoard(...).
        final BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(0L);
        boardDTO.setName("name");
        boardDTO.setColor("color");
        boardDTO.setDescription("description");
        boardDTO.setLastUpdated(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        when(mockBoardService.updateBoard(eq(0L), any(BoardDTO.class))).thenReturn(boardDTO);

        // Run the test and verify the results
        mockMvc.perform(put("/api/user/boards/{id}/update", 0)
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testUpdateBoardById_BoardServiceThrowsResourceNotFoundException() throws Exception {
        // Setup
        when(mockBoardService.updateBoard(eq(0L), any(BoardDTO.class))).thenThrow(ResourceNotFoundException.class);

        // Run the test and verify the results
        mockMvc.perform(put("/api/user/boards/{id}/update", 0)
                        .content("content").contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andExpect(content().json("{}", true));
    }

    @Test
    void testDeleteBoardById() throws Exception {
        // Setup
        // Run the test and verify the results
        mockMvc.perform(delete("/api/user/boards/{id}", 0)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{}", true));
        verify(mockBoardService).deleteBoard(0L);
    }
}
