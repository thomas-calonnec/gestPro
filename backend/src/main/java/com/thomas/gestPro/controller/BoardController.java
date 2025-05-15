package com.thomas.gestPro.controller;

import com.thomas.gestPro.dto.BoardDTO;
import com.thomas.gestPro.dto.ListCardDTO;
import com.thomas.gestPro.model.ListCard;
import com.thomas.gestPro.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/{id}/listCards")
    public ResponseEntity<List<ListCardDTO>> getListCard(@PathVariable Long id) {
        List<ListCardDTO> listCards = boardService.getCardsByBoardId(id);
        return ResponseEntity.ok(listCards);
    }

    @PutMapping("/{id}/listCards")
    public ResponseEntity<ListCard> createListCard(@PathVariable Long id, @RequestBody ListCard listCard) {
        ListCard newListCard = boardService.createListCard(id,listCard);
        return ResponseEntity.ok(newListCard);
    }

    @PutMapping("/{id}/listCards/update")
    public ResponseEntity<List<ListCardDTO>> updateListCard(@PathVariable Long id, @RequestBody List<ListCardDTO> listCard) {
        List<ListCardDTO> newListCard =  boardService.updateListCard(id, listCard);
        return ResponseEntity.ok(newListCard);
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
