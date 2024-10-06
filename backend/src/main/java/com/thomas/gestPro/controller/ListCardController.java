package com.thomas.gestPro.controller;

import com.thomas.gestPro.model.Card;
import com.thomas.gestPro.model.ListCard;
import com.thomas.gestPro.service.ListCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Set;

@RestController
@RequestMapping("/api/listCards")
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
    public ResponseEntity<Set<Card>> getCardsByListCardId(@PathVariable Long id) {
        return ResponseEntity.ok(listCardService.getCardsByListCardId(id));
    }

    @PutMapping("{id}/card")
    public ResponseEntity<Card> createCard(@PathVariable Long id, @RequestBody Card card) {

        listCardService.createCard(id,card);
        return new ResponseEntity<>(card, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ListCard> updateListCardById(@PathVariable Long id, @RequestBody ListCard listCard) {
        ListCard updateListCard =  listCardService.updateListCard(id,listCard);
        return ResponseEntity.ok(updateListCard);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteListCardById(@PathVariable Long id) {
        listCardService.deleteListCard(id);
        return ResponseEntity.noContent().build();
    }
    
}
