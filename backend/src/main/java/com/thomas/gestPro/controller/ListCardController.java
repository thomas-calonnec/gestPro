package com.thomas.gestPro.controller;

import com.thomas.gestPro.dto.CardDTO;
import com.thomas.gestPro.dto.ListCardDTO;
import com.thomas.gestPro.model.Card;
import com.thomas.gestPro.model.ListCard;
import com.thomas.gestPro.service.ListCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/user/listCards")
public class ListCardController {

    private final ListCardService listCardService;

    @Autowired
    public ListCardController(ListCardService listCardService) {
        this.listCardService = listCardService;

    }

    @GetMapping("/{id}")
    public ResponseEntity<ListCard> getListCardById(@PathVariable Long id) {
        ListCard listCard = listCardService.findListCardById(id);
        return listCard != null ? ResponseEntity.ok(listCard) : ResponseEntity.notFound().build();
    }

    @GetMapping("{id}/cards")
    public ResponseEntity<List<CardDTO>> getCardsByListCardId(@PathVariable Long id) {
        return ResponseEntity.ok(listCardService.getCardsByListCardId(id));
    }

    @GetMapping("/board/{id}")
    public ResponseEntity<List<ListCardDTO>> getListCard(@PathVariable Long id) {
        List<ListCardDTO> listCards = listCardService.getCardsByBoardId(id);
        return ResponseEntity.ok(listCards);
    }

    @PostMapping("/board/{id}")
    public ResponseEntity<ListCardDTO> createListCard(@PathVariable Long id, @RequestBody ListCard listCard) {
        ListCardDTO newListCard = listCardService.createListCard(id,listCard);
        return ResponseEntity.ok(newListCard);
    }

//    @PutMapping("/board/{id}")
//    public ResponseEntity<List<ListCardDTO>> updateListCard(@PathVariable Long id, @RequestBody List<ListCardDTO> listCard) {
//        List<ListCardDTO> newListCard =  listCardService.updateListCard(id, listCard);
//        return ResponseEntity.ok(newListCard);
//    }

    @PutMapping("/board/{id}")
    public ResponseEntity<ListCardDTO> updateListCardById(@PathVariable Long id, @RequestBody ListCardDTO listCard) {
        ListCardDTO updateListCard = listCardService.updateListCard(id,listCard);
        return ResponseEntity.ok(updateListCard);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteListCardById(@PathVariable Long id) {
        listCardService.deleteListCard(id);
        return ResponseEntity.noContent().build();
    }

}
