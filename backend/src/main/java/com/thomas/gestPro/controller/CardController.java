package com.thomas.gestPro.controller;

import com.thomas.gestPro.model.Card;
import com.thomas.gestPro.model.Label;
import com.thomas.gestPro.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin("http://192.168.1.138:4200")
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


    @PostMapping("/{id}/setColor")
    public ResponseEntity<Card> addLabelColor(@PathVariable Long id, @RequestBody Label label) {
       Card updateCard = cardService.addCardLabelColor(id,label);
        return ResponseEntity.ok(updateCard);
    }
    @PutMapping("/{id}/label")
    public ResponseEntity<Card> updateLabel(@PathVariable Long id, @RequestBody Label label) {
        Card updateCard = cardService.addCardLabelColor(id,label);
        return ResponseEntity.ok(updateCard);
    }


    @PutMapping("/{id}/update")
    public ResponseEntity<Card> updateCardById(@PathVariable Long id, @RequestBody Card card) {
        Card updateCard = cardService.updateCard(id,card);
        return new ResponseEntity<>(updateCard,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCardById(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}/label")
    public ResponseEntity<Void> removeLabelFromCard(@PathVariable Long id, @RequestBody Label label) {
        cardService.removeLabelFromCard(id,label.getColor());
        return ResponseEntity.noContent().build();
    }

   /* @PostMapping("/clearLabel/{cardId}")
    public ResponseEntity<String> deleteCardAndLabels(@PathVariable Long cardId) {
        cardService.deleteCardAndLabels(cardId);
        return ResponseEntity.ok("Card deleted and labels successfully deleted");
    }*/
}
