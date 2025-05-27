package com.thomas.gestPro.service;


import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.dto.BoardDTO;
import com.thomas.gestPro.dto.CardDTO;
import com.thomas.gestPro.dto.ListCardDTO;
import com.thomas.gestPro.mapper.BoardMapper;
import com.thomas.gestPro.mapper.CardMapper;
import com.thomas.gestPro.mapper.ListCardMapper;
import com.thomas.gestPro.model.Board;
import com.thomas.gestPro.model.Card;
import com.thomas.gestPro.model.ListCard;
import com.thomas.gestPro.repository.BoardRepository;
import com.thomas.gestPro.repository.ListCardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListCardService {

    private final ListCardRepository listCardRepository;
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final ListCardMapper listCardMapper;
    private final CardMapper cardMapper;

    /**
     * Constructor with dependency injection for ListCardRepository and CardRepository.
     *
     * @param listCardRepository repository for managing list of cards
     */
    @Autowired
    public ListCardService(ListCardRepository listCardRepository, BoardRepository boardRepository, BoardMapper boardMapper, ListCardMapper listCardMapper, CardMapper cardMapper) {
        this.listCardRepository = listCardRepository;
        this.boardRepository = boardRepository;
        this.boardMapper = boardMapper;
        this.listCardMapper = listCardMapper;
        this.cardMapper = cardMapper;
    }



    /**
     * Retrieves the set of cards associated with a given ListCard.
     *
     * @param id the ID of the ListCard
     * @return the list of cards associated with the ListCard
     */
    public List<CardDTO> getCardsByListCardId(Long id) {
        return findListCardById(id)
                .getCardList()
                .stream()
                .map(cardMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds a ListCard by its ID.
     *
     * @param id the ID of the ListCard to find
     * @return the found ListCard
     * @throws ResourceNotFoundException if no ListCard is found with the given ID
     */
    public ListCard findListCardById(Long id) {
        return listCardRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ListCard not found with id : " + id));
    }

    /**
     * Updates an existing ListCard.
     *
     * @param listCardId the ID of the ListCard to update
     * @param updateListCard the updated ListCard object containing new details
     * @return the updated ListCard
     */
    public ListCardDTO updateListCard(Long listCardId, ListCardDTO updateListCard){

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
        listCardRepository.save(existingListCard);

        return listCardMapper.toDTO(existingListCard);
    }

    /**
     * Deletes a ListCard by its ID.
     *
     * @param listCardId the ID of the ListCard to delete
     */
    public void deleteListCard(Long listCardId) {
        listCardRepository.deleteById(listCardId);
    }


    /**
     * Finds a board by its ID.
     *
     * @param boardId the ID of the board to retrieve
     * @return the board with the given ID
     * @throws ResourceNotFoundException if the board is not found
     */
    public BoardDTO getBoardById(Long boardId) {
        return boardRepository.findById(boardId).map(boardMapper::toDTO).orElseThrow(() -> new ResourceNotFoundException("Board not found"));
    }

    /**
     * Retrieves the set of list cards associated with a specific board.
     *
     * @param id the ID of the board to retrieve the list cards from
     * @return the list of list cards for the specified board
     * @throws RuntimeException if the board is not found
     */
    public List<ListCardDTO> getCardsByBoardId(Long id) {
        return this.getBoardById(id).getListCards();
    }

    /**
     * Creates a new list card and associates it with a board.
     * This method is transactional, meaning that if anything fails, all changes will be rolled back.
     *
     * @param boardId the ID of the board to associate the list card with
     * @param listCard the new list card to create
     * @return the created list card
     */
    @Transactional
    public ListCardDTO createListCard(Long boardId, ListCard listCard) {
        Board existingboard = boardMapper.toEntity(getBoardById(boardId));
        System.err.println("test " + listCard.getName());
        if(existingboard == null){
            return null;
        }
        listCard.setOrderIndex(existingboard.getListCards().size() + 1);
        listCard.setBoard(existingboard);
        existingboard.setCardCount(existingboard.getListCards().size()+1);

        // existingboard.getListCards().add(listCard);
        //boardRepository.save(existingboard);
        listCardRepository.save(listCard);

        return listCardMapper.toDTO(listCard);
    }

    @Transactional
    public List<ListCardDTO> updateListCard(Long boardId, List<ListCardDTO> listCard) {
        // Récupérer le tableau existant par ID
        Board existingBoard = boardMapper.toEntity(getBoardById(boardId));

        // Mettre à jour chaque ListCard dans la base de données
        for (ListCardDTO card : listCard) {

            // Trouver la carte correspondante dans la liste actuelle du board
            ListCard existingCard = existingBoard.getListCards().stream()
                    .filter(c -> c.getId().equals(card.getId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Carte introuvable : " + card.getId()));

            // Mettre à jour l'ordre de la carte
            existingCard.setOrderIndex(card.getOrderIndex());
            existingCard.setName(card.getName());
            existingCard.setIsArchived(card.getIsArchived());
        }

        // Sauvegarder toutes les cartes mises à jour
        listCardRepository.saveAll(existingBoard.getListCards());

        // Sauvegarder le tableau pour s'assurer que la relation est bien mise à jour
        boardRepository.save(existingBoard);

        return existingBoard.getListCards()
                .stream()
                .map(listCardMapper::toDTO)
                .toList();
    }
}
