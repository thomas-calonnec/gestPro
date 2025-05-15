package com.thomas.gestPro.service;

import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.dto.CardDTO;
import com.thomas.gestPro.dto.CheckListDTO;
import com.thomas.gestPro.dto.LabelDTO;
import com.thomas.gestPro.mapper.CardMapper;
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
    private final CardMapper cardMapper;

    /**
     * Constructor with dependency injection for CardRepository, LabelRepository, and ListCardRepository.
     *
     * @param cardRepository repository for managing cards
     * @param labelRepository repository for managing labels
     * @param listCardRepository repository for managing list of cards
     */
    @Autowired
    public CardService(CardRepository cardRepository, CheckListRepository checkListRepository, LabelRepository labelRepository, ListCardRepository listCardRepository, CardMapper cardMapper) {
        this.cardRepository = cardRepository;
        this.checkListRepository = checkListRepository;
        this.labelRepository = labelRepository;
        this.listCardRepository = listCardRepository;
        this.cardMapper = cardMapper;
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
    public Card updateCard(Long cardId, CardDTO updateCard) {
        // Récupérer l'entité existante
        Card existingCard = getCardById(cardId);

        // Mettre à jour les champs simples
        existingCard.setName(updateCard.getName());
        existingCard.setHours(updateCard.getHours());
        existingCard.setMinutes(updateCard.getMinutes());
        existingCard.setIsCompleted(updateCard.getIsCompleted());
        existingCard.setDeadline(updateCard.getDeadline());
        existingCard.setIsDateActivated(updateCard.getIsDateActivated());
        existingCard.setIsLabelActivated(updateCard.getIsLabelActivated());
        existingCard.setIsChecklistActivated(updateCard.getIsChecklistActivated());
        existingCard.setDescription(updateCard.getDescription());

        // Gestion de la liste CheckList
        List<CheckListDTO> updatedCheckLists = updateCard.getCheckList();
        List<CheckList> existingCheckLists = existingCard.getCheckList();

       // Step 1: Supprimer les checklists supprimées
        existingCheckLists.removeIf(existing ->
                updatedCheckLists.stream().noneMatch(updated ->
                        updated.getId() != null && updated.getId().equals(existing.getId()))
        );

// Step 2: Ajouter ou mettre à jour
        for (CheckListDTO updatedDTO : updatedCheckLists) {
            if (updatedDTO.getId() == null) {
                // Nouvelle checklist
                CheckList newChecklist = new CheckList();
                newChecklist.setName(updatedDTO.getName());
                newChecklist.setCompleted(updatedDTO.getCompleted());
                newChecklist.setCard(existingCard); // relation
                existingCheckLists.add(newChecklist);
            } else {
                // Mise à jour
                existingCheckLists.stream()
                        .filter(existing -> existing.getId().equals(updatedDTO.getId()))
                        .findFirst()
                        .ifPresent(existing -> {
                            existing.setName(updatedDTO.getName());
                            existing.setCompleted(updatedDTO.getCompleted());
                        });
            }
        }

        // Ensure CheckLists are properly saved
        checkListRepository.saveAll(existingCheckLists);

        // Save the updated Card (cascade should handle CheckLists)
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
    public CardDTO addCardLabelColor(Long cardId, LabelDTO updateLabel){
        Label label = labelRepository.findLabelByColor(updateLabel.getColor())
                .orElseThrow(() -> new ResourceNotFoundException("Label not Found"));

        Card existingCard = getCardById(cardId);

        // Ajouter le label à la carte et la carte au label
        existingCard.getLabels().add(label);
        label.getCards().add(existingCard);
        labelRepository.save(label);

        // Sauvegarder uniquement la carte. Hibernate gérera la relation Many-to-Many.
        cardRepository.save(existingCard);

        return cardMapper.toDTO(existingCard);
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

    public Card updateCheckList(Long cardId, CheckListDTO checkList) {
        Card updateCard = this.getCardById(cardId);

        CheckList updateChecklist = updateCard.getCheckList().get(checkList.getId().intValue());
        updateChecklist.setName(checkList.getName());

        checkListRepository.save(updateChecklist);
        return cardRepository.save(updateCard);
    }

}
