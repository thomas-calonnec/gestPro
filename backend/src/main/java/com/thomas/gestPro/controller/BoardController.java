package com.thomas.gestPro.controller;

import com.thomas.gestPro.model.Board;
import com.thomas.gestPro.model.ListCard;
import com.thomas.gestPro.service.BoardService;
import com.thomas.gestPro.service.ListCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api/user/boards")
public class BoardController {

    private final BoardService boardService;
    private final ListCardService listCardService;

    @Autowired
    public BoardController(BoardService boardService, ListCardService listCardService) {
        this.boardService = boardService;
        this.listCardService = listCardService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> getBoardById(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.getBoardById(id));
    }

    @GetMapping("/{id}/listCards")
    public ResponseEntity<List<ListCard>> getListCard(@PathVariable Long id) {
        List<ListCard> listCards = boardService.getCardsByBoardId(id);
        return ResponseEntity.ok(listCards);
    }

    @PutMapping("/{id}/listCards")
    public ResponseEntity<ListCard> createListCard(@PathVariable Long id, @RequestBody ListCard listCard) {
       ListCard newListCard = boardService.createListCard(id,listCard);
        return ResponseEntity.ok(newListCard);
    }

    @PutMapping("/{id}/listCards/update")
    public ResponseEntity<List<ListCard>> updateListCard(@PathVariable Long id, @RequestBody List<ListCard> listCard) {

       List<ListCard> newListCard =  boardService.updateListCard(id, listCard);

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
