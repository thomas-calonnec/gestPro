package com.thomas.gestPro.service;

import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.model.Board;
import com.thomas.gestPro.model.ListCard;
import com.thomas.gestPro.repository.BoardRepository;
import com.thomas.gestPro.repository.ListCardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final ListCardRepository listCardRepository;


    /**
     * Constructor with dependency injection for BoardRepository and ListCardRepository.
     *
     * @param boardRepository repository for managing boards
     * @param listCardRepository repository for managing lists of cards
     */
    @Autowired
    public BoardService(BoardRepository boardRepository, ListCardRepository listCardRepository) {
        this.boardRepository = boardRepository;
        this.listCardRepository = listCardRepository;
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
     * Finds a board by its ID.
     *
     * @param boardName the ID of the board to retrieve
     * @return the board with the given ID
     * @throws ResourceNotFoundException if the board is not found
     */
    public Board getBoardByName(String boardName) {
        return boardRepository.getBoardByName(boardName);
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
     * @param updateBoard the updated board object containing the new name
     * @return the updated board
     * @throws ResourceNotFoundException if the board name is null or empty
     */
    public Board updateBoard(Long id, Board updateBoard){

        Board existingBoard = getBoardById(id);

        if(existingBoard.getName() == null || existingBoard.getName().isEmpty()){
            throw new ResourceNotFoundException("Board not found");
        }
        existingBoard.setName(updateBoard.getName());

        return boardRepository.save(existingBoard);
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
        Board existingboard =  getBoardById(boardId);
       listCard.setBoard(existingboard);

       listCardRepository.save(listCard);
       // existingboard.getListCards().add(listCard);
        boardRepository.save(existingboard);

        return listCard;

    }
}
