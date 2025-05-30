package com.thomas.gestPro.service;

import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.dto.CardDTO;
import com.thomas.gestPro.dto.CheckListDTO;
import com.thomas.gestPro.mapper.CardMapper;
import com.thomas.gestPro.model.Card;
import com.thomas.gestPro.model.CheckList;
import com.thomas.gestPro.model.ListCard;
import com.thomas.gestPro.repository.CardRepository;
import com.thomas.gestPro.repository.CheckListRepository;
import com.thomas.gestPro.repository.ListCardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CheckListRepository checkListRepository;
    private final ListCardRepository listCardRepository;
    private final CardMapper cardMapper;



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
     * Creates a new card and associates it with an existing ListCard.
     *
     * @param listCardId the ID of the ListCard to which the card will be added
     * @param card the card to create and add
     */
    public CardDTO createCard(Long listCardId, Card card) {
        ListCard listCard = findListCardById(listCardId);
        card.setListCard(listCard);
        cardRepository.save(card);
        listCard.getCardList().add(card);
        cardRepository.save(card);
        return cardMapper.toDTO(card);
    }
    /**
     * Finds a ListCard by its ID.
     *
     * @param id the ID of the ListCard to find
     * @return the found ListCard
     * @throws ResourceNotFoundException if no ListCard is found with the given ID
     */
    public ListCard findListCardById(Long id) {
        return listCardRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ListCard not found with id : " + id));
    }
    /**
     * Updates the details of an existing card.
     *
     * @param cardId the ID of the card to update
     * @param updateCard the new card details
     * @return the updated card
     */
    @Transactional
    public CardDTO updateCard(Long cardId, CardDTO updateCard) {
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
        cardRepository.save(existingCard);

        return cardMapper.toDTO(existingCard);
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



    public CardDTO updateCheckList(Long cardId, CheckListDTO checkList) {
        Card updateCard = this.getCardById(cardId);

        CheckList updateChecklist = updateCard.getCheckList().get(checkList.getId().intValue());
        updateChecklist.setName(checkList.getName());

        checkListRepository.save(updateChecklist);
         cardRepository.save(updateCard);
         return cardMapper.toDTO(updateCard);
    }

}
