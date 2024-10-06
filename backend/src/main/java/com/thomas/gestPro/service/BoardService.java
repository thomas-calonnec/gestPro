package com.thomas.gestPro.service;

import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.model.Board;
import com.thomas.gestPro.model.ListCard;
import com.thomas.gestPro.repository.BoardRepository;
import com.thomas.gestPro.repository.ListCardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Set;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final ListCardRepository listCardRepository;


    @Autowired
    public BoardService(BoardRepository boardRepository, ListCardRepository listCardRepository) {
        this.boardRepository = boardRepository;
        this.listCardRepository = listCardRepository;
    }


    public Board getBoardById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new ResourceNotFoundException("Board not found"));
    }


    public Set<ListCard> getCardsByBoardId(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new RuntimeException("Board not found")).getListCards();
    }

    public Board updateBoard(Long id, Board updateBoard){

        Board existingBoard = getBoardById(id);

        if(existingBoard.getBoardName() == null || existingBoard.getBoardName().isEmpty()){
            throw new ResourceNotFoundException("Board not found");
        }
        existingBoard.setBoardName(updateBoard.getBoardName());

        return boardRepository.save(existingBoard);
    }

    public void deleteBoard(Long boardId){
        boardRepository.deleteById(boardId);
    }

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
