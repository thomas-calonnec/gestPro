package com.thomas.gestPro.service;

import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.model.Board;
import com.thomas.gestPro.model.Card;
import com.thomas.gestPro.model.Label;
import com.thomas.gestPro.model.ListCard;
import com.thomas.gestPro.repository.CardRepository;
import com.thomas.gestPro.repository.LabelRepository;
import com.thomas.gestPro.repository.ListCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    private final CardRepository cardRepository;

    private final LabelRepository labelRepository;
    private final ListCardRepository listCardRepository;

    @Autowired
    public CardService(CardRepository cardRepository, LabelRepository labelRepository, ListCardRepository listCardRepository) {
        this.cardRepository = cardRepository;
        this.labelRepository = labelRepository;
        this.listCardRepository = listCardRepository;
    }


    public List<Card> getAllCard() {
        return cardRepository.findAll();
    }
    public Card getCardById(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(() -> new ResourceNotFoundException("Card not found"));
    }


    public Card updateCard(Long cardId, Card updateCard){
        Card existingCard = getCardById(cardId);

        existingCard.setCardName(updateCard.getCardName());
        existingCard.setCardDeadline(updateCard.getCardDeadline());
        existingCard.setCardDescription(updateCard.getCardDescription());
        return cardRepository.save(existingCard);

    }

    public void deleteCard(Long cardId){
        Card card = getCardById(cardId);
        ListCard cardList = listCardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("ListCard not found"));
        cardList.getCardList().remove(card);
        listCardRepository.save(cardList);
        cardRepository.deleteById(cardId);

    }


    public boolean addCardLabelColor(Long cardId, Label updateLabel){
        Label label = labelRepository.findLabelByLabelColor(updateLabel.getLabelColor())
                                        .orElseThrow(() -> new ResourceNotFoundException("Label not Found"));

        Card existingCard = getCardById(cardId);

        // Ajouter le label à la carte et la carte au label
        existingCard.getLabels().add(label);
        label.getCards().add(existingCard);
        labelRepository.save(label);

        // Sauvegarder uniquement la carte. Hibernate gérera la relation Many-to-Many.
        cardRepository.save(existingCard);

        return true;
    }
    public void removeLabelFromCard(Long cardId, Long labelId) {
        // Récupérer la carte
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        // Récupérer le label
        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new RuntimeException("Label not found"));

        // Supprimer le label de la collection de la carte
        card.getLabels().remove(label);

        // Supprimer la carte de la collection du label (pour les relations bidirectionnelles)
        label.getCards().remove(card);

        // Sauvegarder les deux entités pour mettre à jour la relation
        cardRepository.save(card);
        labelRepository.save(label);
    }
   /* public void deleteCardAndLabels(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        // Supprimer tous les labels associés à la carte
        card.getLabels().clear();

        // Sauvegarder les modifications
        cardRepository.save(card);

        // Ensuite, supprimer la carte
        cardRepository.deleteById(cardId);
    }*/
}
