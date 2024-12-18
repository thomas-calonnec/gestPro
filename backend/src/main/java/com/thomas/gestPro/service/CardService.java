package com.thomas.gestPro.service;

import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.model.Card;
import com.thomas.gestPro.model.CheckList;
import com.thomas.gestPro.model.Label;
import com.thomas.gestPro.model.ListCard;
import com.thomas.gestPro.repository.CardRepository;
import com.thomas.gestPro.repository.CheckListRepository;
import com.thomas.gestPro.repository.LabelRepository;
import com.thomas.gestPro.repository.ListCardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final CheckListRepository checkListRepository;
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
    public CardService(CardRepository cardRepository, CheckListRepository checkListRepository, LabelRepository labelRepository, ListCardRepository listCardRepository) {
        this.cardRepository = cardRepository;
        this.checkListRepository = checkListRepository;
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
    @Transactional
    public Card updateCard(Long cardId, Card updateCard) {
        // Récupérer l'entité existante
        Card existingCard = getCardById(cardId);

        // Mettre à jour les champs simples
        existingCard.setName(updateCard.getName());
        existingCard.setHours(updateCard.getHours());
        existingCard.setMinutes(updateCard.getMinutes());
        existingCard.setIsCompleted(updateCard.getIsCompleted());
        existingCard.setDeadline(updateCard.getDeadline());
        existingCard.setDescription(updateCard.getDescription());

        // Gestion de la liste CheckList
        List<CheckList> updatedCheckLists = updateCard.getCheckList();
        List<CheckList> existingCheckLists = existingCard.getCheckList();


        // Supprimer les CheckLists qui ne sont plus présentes
        existingCheckLists.removeIf(existing ->
                updatedCheckLists.stream().noneMatch(updated ->
                        updated.getId() != null && updated.getId().equals(existing.getId()))
        );

        // Ajouter ou mettre à jour les CheckLists existants
        for (CheckList updatedCheckList : updatedCheckLists) {
            if (updatedCheckList.getId() == null) {
                // Nouveau CheckList : l'ajouter
                updatedCheckList.setCard(existingCard); // Maintenir la relation bidirectionnelle
                existingCheckLists.add(updatedCheckList);
            } else {
                // Mettre à jour les CheckLists existants
                existingCheckLists.stream()
                        .filter(existing -> existing.getId().equals(updatedCheckList.getId()))
                        .findFirst()
                        .ifPresent(existing -> {
                            existing.setName(updatedCheckList.getName());
                            existing.setCompleted(updatedCheckList.getCompleted());
                        });
            }
        }

        // Sauvegarder automatiquement via la cascade
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
        Label label = labelRepository.findLabelByColor(updateLabel.getColor())
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
        Label label = labelRepository.findLabelByColor(labelColor)
                .orElseThrow(() -> new RuntimeException("Label not found"));

        // Remove the label from the card's collection
        card.getLabels().remove(label);

        // Remove the card from the label's collection (bidirectional relationship)
        label.getCards().remove(card);

        // Save both entities to update the relationship
        cardRepository.save(card);
        labelRepository.save(label);
    }

    public Card updateCheckList(Long cardId, CheckList checkList) {
        Card updateCard = this.getCardById(cardId);
        System.err.println("card : " + checkList.getName());
        CheckList updateChecklist = updateCard.getCheckList().get(checkList.getId().intValue());
        updateChecklist.setName(checkList.getName());
        System.err.println(updateCard.getCheckList().get(checkList.getId().intValue()).getName());
        checkListRepository.save(checkList);
        return cardRepository.save(updateCard);
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
