package com.thomas.gestPro.controller;

import com.thomas.gestPro.dto.CardDTO;
import com.thomas.gestPro.dto.CheckListDTO;
import com.thomas.gestPro.dto.LabelDTO;
import com.thomas.gestPro.model.Card;
import com.thomas.gestPro.model.Label;
import com.thomas.gestPro.service.CardService;
import com.thomas.gestPro.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/user/cards")
public class CardController {

    private final CardService cardService;
    private final LabelService labelService;

    @Autowired
    public CardController(CardService cardService, LabelService labelService) {
        this.cardService = cardService;
        this.labelService = labelService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCardById(id));
    }

    @PutMapping("{listCardId}/card")
    public ResponseEntity<CardDTO> createCard(@PathVariable Long listCardId, @RequestBody Card card) {
        CardDTO newCard = cardService.createCard(listCardId,card);
        return ResponseEntity.ok(newCard);
    }

    @PostMapping("/{id}/label/create")
    public ResponseEntity<CardDTO> addLabelColor(@PathVariable Long id, @RequestBody LabelDTO label) {
        CardDTO updateCard = labelService.addCardLabelColor(id,label);
        return ResponseEntity.ok(updateCard);
    }

    @PutMapping("/{id}/label")
    public ResponseEntity<CardDTO> updateLabel(@PathVariable Long id, @RequestBody LabelDTO label) {
        CardDTO updateCard = labelService.addCardLabelColor(id,label);
        return ResponseEntity.ok(updateCard);
    }

    @PutMapping("/{id}/check-list/update")
    public ResponseEntity<CardDTO> updateCheckList(@PathVariable Long id, @RequestBody CheckListDTO checkList) {
        CardDTO updateCard = cardService.updateCheckList(id,checkList);
        return ResponseEntity.ok(updateCard);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Card> updateCardById(@PathVariable Long id, @RequestBody CardDTO card) {
        Card updateCard = cardService.updateCard(id,card);
        return new ResponseEntity<>(updateCard,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCardById(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/label/remove")
    public ResponseEntity<Void> removeLabelFromCard(@PathVariable Long id, @RequestBody Label label) {
        labelService.removeLabelFromCard(id,label.getColor());
        return ResponseEntity.noContent().build();
    }

}
