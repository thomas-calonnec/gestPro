package com.thomas.gestPro.controller;

import com.thomas.gestPro.model.Board;
import com.thomas.gestPro.model.ListCard;
import com.thomas.gestPro.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Set;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getBoardById(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getBoardById(id));
    }

    @GetMapping("/{boardName}/listCards")
    public ResponseEntity<Set<ListCard>> getListCard(@PathVariable String boardName) {
        Set<ListCard> listCards = boardService.getCardsByBoardName(boardName);
        return ResponseEntity.ok(listCards);
    }

    @PutMapping("/{id}/listCards")
    public ResponseEntity<ListCard> createListCard(@PathVariable Long id, @RequestBody ListCard listCard) {
       ListCard newListCard = boardService.createListCard(id,listCard);
        return ResponseEntity.ok(newListCard);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Board> updateBoardById(@PathVariable Long id, @RequestBody Board board) {
        Board updateBoard = boardService.updateBoard(id,board);
        return ResponseEntity.ok(updateBoard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBoardById(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }
}
