package com.thomas.gestPro.service;

import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.model.Card;
import com.thomas.gestPro.model.Label;
import com.thomas.gestPro.model.ListCard;
import com.thomas.gestPro.repository.CardRepository;
import com.thomas.gestPro.repository.LabelRepository;
import com.thomas.gestPro.repository.ListCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;

    private final LabelRepository labelRepository;
    private final ListCardRepository listCardRepository;

    /**
     * Constructor with dependency injection for CardRepository, LabelRepository, and ListCardRepository.
     *
     * @param cardRepository repository for managing cards
     * @param labelRepository repository for managing labels
     * @param listCardRepository repository for managing list of cards
     */
    @Autowired
    public CardService(CardRepository cardRepository, LabelRepository labelRepository, ListCardRepository listCardRepository) {
        this.cardRepository = cardRepository;
        this.labelRepository = labelRepository;
        this.listCardRepository = listCardRepository;
    }


    /**
     * Retrieves all the cards.
     *
     * @return a list of all cards
     */
    public List<Card> getAllCard() {
        return cardRepository.findAll();
    }

    /**
     * Finds a card by its ID.
     *
     * @param cardId the ID of the card to retrieve
     * @return the card with the given ID
     * @throws ResourceNotFoundException if the card is not found
     */
    public Card getCardById(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(() -> new ResourceNotFoundException("Card not found"));
    }


    /**
     * Updates the details of an existing card.
     *
     * @param cardId the ID of the card to update
     * @param updateCard the new card details
     * @return the updated card
     */
    public Card updateCard(Long cardId, Card updateCard){
        Card existingCard = getCardById(cardId);

        existingCard.setCardName(updateCard.getCardName());
        existingCard.setCardDeadline(updateCard.getCardDeadline());
        existingCard.setCardDescription(updateCard.getCardDescription());
        return cardRepository.save(existingCard);

    }

    /**
     * Deletes a card by its ID and removes it from the associated list of cards.
     *
     * @param cardId the ID of the card to delete
     */
    public void deleteCard(Long cardId){
        // Retrieve the card and its list
        Card card = getCardById(cardId);
        ListCard cardList = listCardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("ListCard not found"));

        // Remove the card from the list
        cardList.getCardList().remove(card);

        // Save the updated list and delete the card
        listCardRepository.save(cardList);
        cardRepository.deleteById(cardId);

    }


    /**
     * Adds a label with a specific color to a card.
     *
     * @param cardId the ID of the card
     * @param updateLabel the label to add to the card
     * @return the updated card with the label added
     * @throws ResourceNotFoundException if the label is not found
     */
    public Card addCardLabelColor(Long cardId, Label updateLabel){
        Label label = labelRepository.findLabelByLabelColor(updateLabel.getLabelColor())
                                        .orElseThrow(() -> new ResourceNotFoundException("Label not Found"));

        Card existingCard = getCardById(cardId);

        // Ajouter le label à la carte et la carte au label
        existingCard.getLabels().add(label);
        label.getCards().add(existingCard);
        labelRepository.save(label);

        // Sauvegarder uniquement la carte. Hibernate gérera la relation Many-to-Many.
        cardRepository.save(existingCard);

        return existingCard;
    }

    /**
     * Removes a label with a specific color from a card.
     *
     * @param cardId the ID of the card
     * @param labelColor the color of the label to remove
     */
    public void removeLabelFromCard(Long cardId, String labelColor) {
        // Retrieve the card by ID
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        // Retrieve the label by color
        Label label = labelRepository.findLabelByLabelColor(labelColor)
                .orElseThrow(() -> new RuntimeException("Label not found"));

        // Remove the label from the card's collection
        card.getLabels().remove(label);

        // Remove the card from the label's collection (bidirectional relationship)
        label.getCards().remove(card);

        // Save both entities to update the relationship
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
