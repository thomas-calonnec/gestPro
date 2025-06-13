package com.thomas.gestPro.service.integration;

import com.thomas.gestPro.dto.ListCardDTO;
import com.thomas.gestPro.mapper.BoardMapper;
import com.thomas.gestPro.mapper.ListCardMapper;
import com.thomas.gestPro.model.Board;
import com.thomas.gestPro.model.ListCard;
import com.thomas.gestPro.repository.BoardRepository;
import com.thomas.gestPro.repository.ListCardRepository;
import com.thomas.gestPro.service.ListCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ListCardServiceIntegrationTest {

    @Autowired
    private ListCardService listCardService;

    @Autowired
    private ListCardRepository listCardRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardMapper boardMapper;

    @Autowired
    private ListCardMapper listCardMapper;

    private Board testBoard;
    private ListCard testListCard;


    @BeforeEach
    void setUp() {
        testBoard = Board.builder()
                .name("Test Board")
                .color("#ffffff")
                .description("Test Description")
                .lastUpdated(new Date())
                .cardCount(0)
                .status("PENDING")
                .build();
        boardRepository.save(testBoard);

        testListCard = ListCard.builder()
                .name("Test List")
                .orderIndex(0)
                .board(testBoard)
                .build();
        listCardRepository.save(testListCard);
    }

    @Test
    void createListCard_ShouldCreateNewListCard() {

        ListCardDTO newListCard = ListCardDTO.builder()
                .name("New List")
                .orderIndex(1)
                .board(boardMapper.toLightDTO(testBoard))
                .build();


        ListCardDTO savedListCard = listCardService.createListCard(testBoard.getId(), listCardMapper.toEntity(newListCard));

        assertNotNull(savedListCard.getId());
        assertEquals("New List", savedListCard.getName());
        assertEquals(1, savedListCard.getOrderIndex());
        assertEquals(testBoard.getId(), savedListCard.getBoard().getId());
    }

    @Test
    void updateListCard_ShouldUpdateExistingListCard() {
        testListCard.setName("Updated List");
        ListCardDTO updatedListCard = listCardService.updateListCard(testListCard.getId(), listCardMapper.toDTO(testListCard));

        assertEquals("Updated List", updatedListCard.getName());
        assertEquals(testListCard.getId(), updatedListCard.getId());
    }

    @Test
    void deleteListCard_ShouldRemoveListCard() {
        listCardService.deleteListCard(testListCard.getId());

        assertFalse(listCardRepository.existsById(testListCard.getId()));
    }

    @Test
    void getListCardsByBoardId_ShouldReturnAllListCards() {
        List<ListCardDTO> listCards = listCardService.getCardsByBoardId(testBoard.getId());
        System.out.println(listCardRepository.findAll());
        System.err.println(boardRepository.findAll());
        assertFalse(listCards.isEmpty());
        assertTrue(listCards.stream().allMatch(list -> list.getBoard().getId().equals(testBoard.getId())));
    }

    @Test
    void updateListCardPosition_ShouldUpdatePosition() {
        testListCard.setOrderIndex(2);
        ListCardDTO movedListCard = listCardService.updateListCard(testListCard.getId(),listCardMapper.toDTO(testListCard));

        assertEquals(2, movedListCard.getOrderIndex());
    }
}
