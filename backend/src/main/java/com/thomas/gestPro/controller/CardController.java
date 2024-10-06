package com.thomas.gestPro.controller;

import com.thomas.gestPro.model.Card;
import com.thomas.gestPro.model.Label;
import com.thomas.gestPro.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> getCardById(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCardById(id));
    }


    @GetMapping("/listCards")
    public ResponseEntity<List<Card>> getListOfCard() {
        return ResponseEntity.ok(cardService.getAllCard());
    }

    @PostMapping("/{id}/setColor")
    public ResponseEntity<String> addLabelColor(@PathVariable Long id, @RequestBody Label label) {

       boolean response = cardService.addCardLabelColor(id,label);

        return response ? ResponseEntity.ok("Color success") : ResponseEntity.badRequest().body("Color not found");
    }


    @PutMapping("/{id}/update")
    public ResponseEntity<Card> updateCardById(@PathVariable Long id, @RequestBody Card card) {
        cardService.updateCard(id,card);
        return new ResponseEntity<>(card,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCardById(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.ok("Card deleted");
    }
    @DeleteMapping("/{id}/deleteLabel")
    public ResponseEntity<String> removeLabelFromCard(@PathVariable Long id, @RequestBody Label label) {
        cardService.removeLabelFromCard(id,label.getLabelId());
        return ResponseEntity.noContent().build();
    }

   /* @PostMapping("/clearLabel/{cardId}")
    public ResponseEntity<String> deleteCardAndLabels(@PathVariable Long cardId) {
        cardService.deleteCardAndLabels(cardId);
        return ResponseEntity.ok("Card deleted and labels successfully deleted");
    }*/
}
