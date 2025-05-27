package com.thomas.gestPro.controller;

import com.thomas.gestPro.dto.BoardDTO;
import com.thomas.gestPro.dto.ListCardDTO;
import com.thomas.gestPro.model.ListCard;
import com.thomas.gestPro.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api/user/boards")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> getBoardById(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getBoardById(id));
    }

    @PostMapping("{id}/board")
    public ResponseEntity<BoardDTO> createBoard(@PathVariable Long id, @RequestBody BoardDTO board) {
        BoardDTO newBoard = boardService.createBoard(id,board);
        return new ResponseEntity<>(newBoard, HttpStatus.CREATED);
    }

    @GetMapping("/workspace/{id}")
    public ResponseEntity<List<BoardDTO>> getListBoardByWorkspaceId(@PathVariable Long id) {
        List<BoardDTO> boards = boardService.getListBoardByWorkspaceId(id);
        return ResponseEntity.ok(boards);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<BoardDTO> updateBoardById(@PathVariable Long id, @RequestBody BoardDTO board) {
        BoardDTO updateBoard = boardService.updateBoard(id,board);
        return ResponseEntity.ok(updateBoard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBoardById(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }
}
