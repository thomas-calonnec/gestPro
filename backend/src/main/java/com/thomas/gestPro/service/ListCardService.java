package com.thomas.gestPro.service;


import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.model.Board;
import com.thomas.gestPro.model.Card;
import com.thomas.gestPro.model.ListCard;
import com.thomas.gestPro.repository.BoardRepository;
import com.thomas.gestPro.repository.CardRepository;
import com.thomas.gestPro.repository.ListCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ListCardService {

    private final ListCardRepository listCardRepository;
    private final BoardRepository boardRepository;
    private final CardRepository cardRepository;

    /**
     * Constructor with dependency injection for ListCardRepository and CardRepository.
     *
     * @param listCardRepository repository for managing list of cards
     * @param cardRepository repository for managing cards
     */
    @Autowired
    public ListCardService(ListCardRepository listCardRepository, CardRepository cardRepository, BoardRepository boardRepository) {
        this.listCardRepository = listCardRepository;
        this.boardRepository = boardRepository;
        this.cardRepository = cardRepository;
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
     * Retrieves the set of cards associated with a given ListCard.
     *
     * @param id the ID of the ListCard
     * @return the list of cards associated with the ListCard
     */
    public List<Card> getCardsByListCardId(Long id) {
        return findListCardById(id).getCardList();
    }

    /**
     * Creates a new card and associates it with an existing ListCard.
     *
     * @param listCardId the ID of the ListCard to which the card will be added
     * @param card the card to create and add
     */

    public Card createCard(Long listCardId, Card card) {
        ListCard listCard = findListCardById(listCardId);
        card.setListCard(listCard);
         cardRepository.save(card);
        listCard.getCardList().add(card);

        return cardRepository.save(card);
    }

    /**
     * Updates an existing ListCard.
     *
     * @param listCardId the ID of the ListCard to update
     * @param updateListCard the updated ListCard object containing new details
     * @return the updated ListCard
     */
    public ListCard updateListCard(Long listCardId, ListCard updateListCard){

        ListCard existingListCard = findListCardById(listCardId);

        existingListCard.setName(updateListCard.getName());
        existingListCard.setOrderIndex(updateListCard.getOrderIndex());

        existingListCard.setIsArchived(updateListCard.getIsArchived());
        // Si le board est modifié, vérifier et mettre à jour
        if (updateListCard.getBoard() != null && !updateListCard.getBoard().getId().equals(existingListCard.getBoard().getId())) {

            Board board = boardRepository.findById(updateListCard.getBoard().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + updateListCard.getBoard().getId()));
            existingListCard.setBoard(board);
        }
        return listCardRepository.save(updateListCard);
    }

    /**
     * Deletes a ListCard by its ID.
     *
     * @param listCardId the ID of the ListCard to delete
     */
    public void deleteListCard(Long listCardId) {
        listCardRepository.deleteById(listCardId);
    }


}
