package com.thomas.gestPro.service;

import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.dto.BoardDTO;
import com.thomas.gestPro.dto.ListCardDTO;
import com.thomas.gestPro.mapper.BoardMapper;
import com.thomas.gestPro.mapper.ListCardMapper;
import com.thomas.gestPro.mapper.UserMapper;
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
    private final ListCardMapper listCardMapper;
    private final BoardMapper boardMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;


    /**
     * Constructor with dependency injection for BoardRepository and ListCardRepository.
     *
     * @param boardRepository repository for managing boards
     * @param listCardRepository repository for managing lists of cards
     */
    @Autowired
    public BoardService(BoardRepository boardRepository, ListCardRepository listCardRepository, ListCardMapper listCardMapper, BoardMapper boardMapper, UserMapper userMapper, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.listCardRepository = listCardRepository;
        this.listCardMapper = listCardMapper;
        this.boardMapper = boardMapper;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
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
     * Updates the name of an existing board.
     *
     * @param id the ID of the board to update
     * @param boardInput the updated board object containing the new name
     * @return the updated board
     * @throws ResourceNotFoundException if the board name is null or empty
     */
    public BoardDTO updateBoard(Long id, BoardDTO boardInput){

        // Charger les membres depuis la base pour éviter les entités détachées
        List<User> attachedMembers = boardInput.getMembers()
                .stream()
                .map(userMapper::toEntity)
                .toList();


        Board board = boardMapper.toEntity(this.getBoardById(id));
        board.setName(boardInput.getName());
        board.setDescription(boardInput.getDescription());
        board.setLastUpdated(new Date());
        board.setStatus(boardInput.getStatus());
//        board.setMembers(new ArrayList<>(attachedMembers));
        board.setOwnerId(boardInput.getOwnerId());

//        attachedMembers.forEach(user -> user.getBoards().add(board));

        boardRepository.save(board);
        return boardMapper.toDTO(board);
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
        Board existingboard = boardMapper.toEntity(getBoardById(boardId));
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
