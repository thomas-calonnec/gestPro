package com.thomas.gestPro.service;

import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.model.Board;
import com.thomas.gestPro.model.ListCard;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.repository.BoardRepository;
import com.thomas.gestPro.repository.ListCardRepository;
import com.thomas.gestPro.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final ListCardRepository listCardRepository;
    private final UserRepository userRepository;


    /**
     * Constructor with dependency injection for BoardRepository and ListCardRepository.
     *
     * @param boardRepository repository for managing boards
     * @param listCardRepository repository for managing lists of cards
     */
    @Autowired
    public BoardService(BoardRepository boardRepository, ListCardRepository listCardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.listCardRepository = listCardRepository;
        this.userRepository = userRepository;
    }


    /**
     * Finds a board by its ID.
     *
     * @param boardId the ID of the board to retrieve
     * @return the board with the given ID
     * @throws ResourceNotFoundException if the board is not found
     */
    public Board getBoardById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new ResourceNotFoundException("Board not found"));
    }

    /**
     * Retrieves the set of list cards associated with a specific board.
     *
     * @param id the ID of the board to retrieve the list cards from
     * @return the list of list cards for the specified board
     * @throws RuntimeException if the board is not found
     */
    public List<ListCard> getCardsByBoardId(Long id) {
        return this.getBoardById(id).getListCards();
    }

    /**
     * Updates the name of an existing board.
     *
     * @param id the ID of the board to update
     * @param boardInput the updated board object containing the new name
     * @return the updated board
     * @throws ResourceNotFoundException if the board name is null or empty
     */
    public Board updateBoard(Long id, Board boardInput){

        // Charger les membres depuis la base pour éviter les entités détachées
        List<User> attachedMembers = boardInput.getMembers().stream()
                .map(user -> userRepository.findById(user.getId())
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + user.getId())))
                .toList();

        Board board = boardRepository.getReferenceById(id);
        board.setName(boardInput.getName());
        board.setDescription(boardInput.getDescription());
        board.setLastUpdated(new Date());

        board.setMembers(attachedMembers);
        board.setOwnerId(boardInput.getOwnerId());

        attachedMembers.forEach(user -> user.getBoards().add(board));

        boardRepository.save(board);
        return board;
    }

    /**
     * Deletes a board by its ID.
     *
     * @param boardId the ID of the board to delete
     */
    public void deleteBoard(Long boardId){
        boardRepository.deleteById(boardId);
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
    public ListCard createListCard(Long boardId, ListCard listCard) {
        Board existingboard = getBoardById(boardId);
        if(existingboard == null){
            return null;
        }
        listCard.setOrderIndex(existingboard.getListCards().size() + 1);
        listCard.setBoard(existingboard);
        existingboard.setCardCount(existingboard.getListCards().size()+1);

       // existingboard.getListCards().add(listCard);
        //boardRepository.save(existingboard);
        return listCardRepository.save(listCard);
    }
    @Transactional
    public List<ListCard> updateListCard(Long boardId, List<ListCard> listCard) {
        // Récupérer le tableau existant par ID
        Board existingBoard = getBoardById(boardId);

        // Mettre à jour chaque ListCard dans la base de données
        for (ListCard card : listCard) {

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

        return existingBoard.getListCards();
    }


}
