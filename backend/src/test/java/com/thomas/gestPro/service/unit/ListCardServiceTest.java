package com.thomas.gestPro.service.unit;

import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.dto.BoardDTO;
import com.thomas.gestPro.dto.BoardLightDTO;
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
import com.thomas.gestPro.service.ListCardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ListCardServiceTest {

    @Mock
    private ListCardRepository mockListCardRepository;
    @Mock
    private BoardRepository mockBoardRepository;
    @Mock
    private BoardMapper mockBoardMapper;
    @Mock
    private ListCardMapper mockListCardMapper;
    @Mock
    private CardMapper mockCardMapper;

    private ListCardService listCardServiceUnderTest;

    @BeforeEach
    void setUp() {
        listCardServiceUnderTest = new ListCardService(mockListCardRepository, mockBoardRepository, mockBoardMapper,
                mockListCardMapper, mockCardMapper);
    }

    @Test
    void testGetCardsByListCardId() {
        // Setup
        // Configure ListCardRepository.findById(...).
        final Optional<ListCard> listCard = Optional.of(ListCard.builder()
                .id(0L)
                .name("name")
                .orderIndex(0)
                .isArchived(false)
                .board(Board.builder()
                        .id(0L)
                        .cardCount(0)
                        .listCards(List.of())
                        .build())
                .cardList(List.of(Card.builder().build()))
                .build());
        when(mockListCardRepository.findById(0L)).thenReturn(listCard);

        // Configure CardMapper.toDTO(...).
        final CardDTO cardDTO = new CardDTO();
        cardDTO.setId(0L);
        cardDTO.setName("name");
        cardDTO.setDescription("description");
        cardDTO.setDeadline(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        cardDTO.setHours(0);
        when(mockCardMapper.toDTO(Card.builder().build())).thenReturn(cardDTO);

        // Run the test
        final List<CardDTO> result = listCardServiceUnderTest.getCardsByListCardId(0L);

        // Verify the results
    }

    @Test
    void testGetCardsByListCardId_ListCardRepositoryReturnsAbsent() {
        // Setup
        when(mockListCardRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> listCardServiceUnderTest.getCardsByListCardId(0L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testFindListCardById() {
        // Setup
        final ListCard expectedResult = ListCard.builder()
                .id(0L)
                .name("name")
                .orderIndex(0)
                .isArchived(false)
                .board(Board.builder()
                        .id(0L)
                        .cardCount(0)
                        .listCards(new ArrayList<>(List.of()))
                        .build())
                .cardList(new ArrayList<>(List.of(Card.builder().build())))
                .build();

        // Configure ListCardRepository.findById(...).
        final Optional<ListCard> listCard = Optional.of(ListCard.builder()
                .id(0L)
                .name("name")
                .orderIndex(0)
                .isArchived(false)
                .board(Board.builder()
                        .id(0L)
                        .cardCount(0)
                        .listCards(new ArrayList<>(List.of()))
                        .build())
                .cardList(new ArrayList<>(List.of(Card.builder().build())))
                .build());
        when(mockListCardRepository.findById(0L)).thenReturn(listCard);

        // Run the test
        final ListCard result = listCardServiceUnderTest.findListCardById(0L);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testFindListCardById_ListCardRepositoryReturnsAbsent() {
        // Setup
        when(mockListCardRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> listCardServiceUnderTest.findListCardById(0L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void shouldUpdateListCardAndSaveIt() {
        // Arrange
        long listCardId = 0L;
        ListCardDTO inputDTO = createListCardDto(listCardId, "name");
        ListCard entity = createListCardEntity(listCardId, "name");
        Board board = createBoardEntity(listCardId, entity);

        when(mockListCardRepository.findById(listCardId)).thenReturn(Optional.of(entity));
        when(mockBoardRepository.findById(listCardId)).thenReturn(Optional.of(board));
        when(mockListCardMapper.toDTO(any(ListCard.class))).thenReturn(inputDTO);

        // Act
        listCardServiceUnderTest.updateListCard(listCardId, inputDTO);

        // Assert
        verify(mockListCardRepository).save(any(ListCard.class)); // On vérifie juste la sauvegarde
    }

    private ListCardDTO createListCardDto(long id, String name) {
        ListCardDTO dto = new ListCardDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setOrderIndex(0);
        dto.setIsArchived(false);
        BoardLightDTO boardDTO = new BoardLightDTO();
        boardDTO.setId(id);
        dto.setBoard(boardDTO);
        return dto;
    }

    private ListCard createListCardEntity(long id, String name) {
        return ListCard.builder()
                .id(id)
                .name(name)
                .orderIndex(0)
                .isArchived(false)
                .board(Board.builder().id(id).cardCount(0).listCards(List.of()).build())
                .cardList(List.of(Card.builder().build()))
                .build();
    }
    private ListCard createListCardEntity(long id, String name, Board board) {
        return ListCard.builder()
                .id(id)
                .name(name)
                .orderIndex(0)
                .isArchived(false)
                .board(board)
                .cardList(List.of(Card.builder().build()))
                .build();
    }

    private Board createBoardEntity(long id, ListCard listCard) {
        return Board.builder()
                .id(id)
                .cardCount(0)
                .listCards(List.of(listCard))
                .build();
    }

    @Test
    void testUpdateListCard1_ListCardRepositoryFindByIdReturnsAbsent() {
        // Setup
        final ListCardDTO updateListCard = new ListCardDTO();
        updateListCard.setId(0L);
        updateListCard.setName("name");
        updateListCard.setOrderIndex(0);
        updateListCard.setIsArchived(false);
        final BoardLightDTO board = new BoardLightDTO();
        board.setId(0L);
        updateListCard.setBoard(board);

        when(mockListCardRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> listCardServiceUnderTest.updateListCard(0L, updateListCard))
                .isInstanceOf(ResourceNotFoundException.class);
    }




    @Test
    void shouldThrowResourceNotFoundWhenBoardIsAbsentOnUpdate() {
        long listCardId = 0L;
        long currentBoardId = 1L;
        long boardIdToUpdate = 99L;

        // Board existant pour la ListCard
        Board currentBoard = Board.builder().id(currentBoardId).build();
        ListCard existingListCard = createListCardEntity(listCardId, "name", currentBoard);

        // Nouveau board à affecter dans le DTO
        BoardLightDTO updateBoardDTO = BoardLightDTO.builder().id(boardIdToUpdate).build();
        ListCardDTO updateListCardDTO = createListCardDto(listCardId, "name");
        updateListCardDTO.setBoard(updateBoardDTO);

        when(mockListCardRepository.findById(listCardId)).thenReturn(Optional.of(existingListCard));
        when(mockBoardRepository.findById(boardIdToUpdate)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> listCardServiceUnderTest.updateListCard(listCardId, updateListCardDTO))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testDeleteListCard() {
        // Setup
        // Run the test
        listCardServiceUnderTest.deleteListCard(0L);

        // Verify the results
        verify(mockListCardRepository).deleteById(0L);
    }

    @Test
    void testGetBoardById() {
        // Setup
        // Configure BoardRepository.findById(...).
        final Optional<Board> board = Optional.of(Board.builder()
                .id(0L)
                .cardCount(0)
                .listCards(List.of(ListCard.builder()
                        .id(0L)
                        .name("name")
                        .orderIndex(0)
                        .isArchived(false)
                        .cardList(List.of(Card.builder().build()))
                        .build()))
                .build());
        when(mockBoardRepository.findById(0L)).thenReturn(board);

        // Configure BoardMapper.toDTO(...).
        final BoardDTO boardDTO = new BoardDTO();
        final ListCardDTO listCardDTO = new ListCardDTO();
        listCardDTO.setId(0L);
        listCardDTO.setName("name");
        listCardDTO.setOrderIndex(0);
        listCardDTO.setIsArchived(false);
        final BoardLightDTO board1 = new BoardLightDTO();
        board1.setId(0L);
        listCardDTO.setBoard(board1);
        boardDTO.setListCards(List.of(listCardDTO));
        when(mockBoardMapper.toDTO(any(Board.class))).thenReturn(boardDTO);

        // Run the test
        final BoardDTO result = listCardServiceUnderTest.getBoardById(0L);

        // Verify the results
    }

    @Test
    void testGetBoardById_BoardRepositoryReturnsAbsent() {
        // Setup
        when(mockBoardRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> listCardServiceUnderTest.getBoardById(0L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testGetCardsByBoardId() {
        // Setup
        // Configure BoardRepository.findById(...).
        final Optional<Board> board = Optional.of(Board.builder()
                .id(0L)
                .cardCount(0)
                .listCards(List.of(ListCard.builder()
                        .id(0L)
                        .name("name")
                        .orderIndex(0)
                        .isArchived(false)
                        .cardList(List.of(Card.builder().build()))
                        .build()))
                .build());
        when(mockBoardRepository.findById(0L)).thenReturn(board);

        // Configure BoardMapper.toDTO(...).
        final BoardDTO boardDTO = new BoardDTO();
        final ListCardDTO listCardDTO = new ListCardDTO();
        listCardDTO.setId(0L);
        listCardDTO.setName("name");
        listCardDTO.setOrderIndex(0);
        listCardDTO.setIsArchived(false);
        final BoardLightDTO board1 = new BoardLightDTO();
        board1.setId(0L);
        listCardDTO.setBoard(board1);
        boardDTO.setListCards(List.of(listCardDTO));
        when(mockBoardMapper.toDTO(any(Board.class))).thenReturn(boardDTO);

        // Run the test
        final List<ListCardDTO> result = listCardServiceUnderTest.getCardsByBoardId(0L);

        // Verify the results
    }

    @Test
    void testGetCardsByBoardId_BoardRepositoryReturnsAbsent() {
        // Setup
        when(mockBoardRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> listCardServiceUnderTest.getCardsByBoardId(0L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testCreateListCard() {
        // Préparation
        var board = createBoard(0L);
        var boardDTO = createBoardDTO(0L); // Crée un DTO correspondant

        var listCard = createListCard(0L, board);

        // Mocks
        when(mockBoardRepository.save(board)).thenReturn(board);
        when(mockBoardRepository.findById(0L)).thenReturn(Optional.of(board));
        when(mockBoardMapper.toDTO(board)).thenReturn(boardDTO); // Ajouté
        when(mockBoardMapper.toEntity(boardDTO)).thenReturn(board); // Ajouté
        when(mockListCardMapper.toDTO(any(ListCard.class)))
                .thenReturn(new ListCardDTO(0L, "name", 0, false, null, List.of()));

        // Exécution
        listCardServiceUnderTest.createListCard(0L, listCard);

        // Vérification
        verify(mockListCardRepository).save(any(ListCard.class));
    }

    private BoardDTO createBoardDTO(long id) {
        BoardDTO dto = new BoardDTO();
        dto.setId(id);
        dto.setName("Test Board " + id);
        dto.setColor("blue");
        dto.setDescription("Description du Board " + id);
        dto.setLastUpdated(new Date());
        dto.setCardCount(0);
        dto.setStatus("PENDING");
        dto.setOwnerId(1L);
        dto.setListCards(new ArrayList<>()); // ou List.of()
        dto.setMembers(new ArrayList<>());   // ou List.of()
        return dto;
    }


    // Méthodes utilitaires privées pour éviter la répétition
    private Board createBoard(Long id) {
        return Board.builder()
                .id(id)
                .cardCount(0)
                .listCards(new ArrayList<>())
                .build();
    }

    private ListCard createListCard(Long id, Board board) {
        return ListCard.builder()
                .id(id)
                .name("name")
                .orderIndex(0)
                .isArchived(false)
                .board(board)
                .cardList(new ArrayList<>())
                .build();
    }

    @Test
    void testCreateListCard_BoardRepositoryReturnsAbsent() {
        // Setup
        final ListCard listCard = ListCard.builder()
                .id(0L)
                .name("name")
                .orderIndex(0)
                .isArchived(false)
                .board(Board.builder()
                        .id(0L)
                        .cardCount(0)
                        .listCards(List.of())
                        .build())
                .cardList(List.of(Card.builder().build()))
                .build();
        when(mockBoardRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> listCardServiceUnderTest.createListCard(0L, listCard))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testCreateListCard_BoardMapperToEntityReturnsNull() {
        // Setup
        final ListCard listCard = ListCard.builder()
                .id(0L)
                .name("name")
                .orderIndex(0)
                .isArchived(false)
                .board(Board.builder()
                        .id(0L)
                        .cardCount(0)
                        .listCards(List.of())
                        .build())
                .cardList(List.of(Card.builder().build()))
                .build();

        // Configure BoardRepository.findById(...).
        final Optional<Board> board = Optional.of(Board.builder()
                .id(0L)
                .cardCount(0)
                .listCards(List.of(ListCard.builder()
                        .id(0L)
                        .name("name")
                        .orderIndex(0)
                        .isArchived(false)
                        .cardList(List.of(Card.builder().build()))
                        .build()))
                .build());
        when(mockBoardRepository.findById(0L)).thenReturn(board);

        // Configure BoardMapper.toDTO(...).
        final BoardDTO boardDTO = new BoardDTO();
        final ListCardDTO listCardDTO = new ListCardDTO();
        listCardDTO.setId(0L);
        listCardDTO.setName("name");
        listCardDTO.setOrderIndex(0);
        listCardDTO.setIsArchived(false);
        final BoardLightDTO board1 = new BoardLightDTO();
        board1.setId(0L);
        listCardDTO.setBoard(board1);
        boardDTO.setListCards(List.of(listCardDTO));
        when(mockBoardMapper.toDTO(any(Board.class))).thenReturn(boardDTO);

        when(mockBoardMapper.toEntity(any(BoardDTO.class))).thenReturn(null);

        // Run the test
        final ListCardDTO result = listCardServiceUnderTest.createListCard(0L, listCard);

        // Verify the results
        assertThat(result).isNull();
    }

    @Test
    void testCreateListCard_ListCardRepositoryThrowsOptimisticLockingFailureException() {
        // Setup
        final ListCard listCard = ListCard.builder()
                .id(0L)
                .name("name")
                .orderIndex(0)
                .isArchived(false)
                .board(Board.builder()
                        .id(0L)
                        .cardCount(0)
                        .listCards(List.of())
                        .build())
                .cardList(List.of(Card.builder().build()))
                .build();

        // Configure BoardRepository.findById(...).
        final Optional<Board> board = Optional.of(Board.builder()
                .id(0L)
                .cardCount(0)
                .listCards(List.of(ListCard.builder()
                        .id(0L)
                        .name("name")
                        .orderIndex(0)
                        .isArchived(false)
                        .cardList(List.of(Card.builder().build()))
                        .build()))
                .build());
        when(mockBoardRepository.findById(0L)).thenReturn(board);

        // Configure BoardMapper.toDTO(...).
        final BoardDTO boardDTO = new BoardDTO();
        final ListCardDTO listCardDTO = new ListCardDTO();
        listCardDTO.setId(0L);
        listCardDTO.setName("name");
        listCardDTO.setOrderIndex(0);
        listCardDTO.setIsArchived(false);
        final BoardLightDTO board1 = new BoardLightDTO();
        board1.setId(0L);
        listCardDTO.setBoard(board1);
        boardDTO.setListCards(List.of(listCardDTO));
        when(mockBoardMapper.toDTO(any(Board.class))).thenReturn(boardDTO);

        // Configure BoardMapper.toEntity(...).
        final Board board2 = Board.builder()
                .id(0L)
                .cardCount(0)
                .listCards(List.of(ListCard.builder()
                        .id(0L)
                        .name("name")
                        .orderIndex(0)
                        .isArchived(false)
                        .cardList(List.of(Card.builder().build()))
                        .build()))
                .build();
        when(mockBoardMapper.toEntity(any(BoardDTO.class))).thenReturn(board2);

        when(mockListCardRepository.save(ListCard.builder()
                .id(0L)
                .name("name")
                .orderIndex(0)
                .isArchived(false)
                .board(Board.builder()
                        .id(0L)
                        .cardCount(0)
                        .listCards(List.of())
                        .build())
                .cardList(List.of(Card.builder().build()))
                .build())).thenThrow(OptimisticLockingFailureException.class);

        // Run the test
        assertThatThrownBy(() -> listCardServiceUnderTest.createListCard(0L, listCard))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    @Test
    void shouldUpdateListCardsAndSaveBoard() {
        // Préparation des données utiles
        long boardId = 0L;
        long listCardId = 0L;
        ListCardDTO listCardDTO = buildListCardDto(listCardId, boardId, "name");
        List<ListCardDTO> dtoList = List.of(listCardDTO);

        Board boardEntity = Board.builder().id(boardId).cardCount(0).build();
        ListCard listCardEntity = ListCard.builder().id(listCardId).name("name").orderIndex(0).isArchived(false)
                .board(boardEntity).cardList(List.of(Card.builder().build())).build();
        boardEntity.setListCards(List.of(listCardEntity));

        when(mockBoardRepository.findById(boardId)).thenReturn(Optional.of(boardEntity));

        // Mapping mocks (génériques dans ce scénario, ici on peut simplifier)
        when(mockBoardMapper.toDTO(any(Board.class))).thenReturn(new BoardDTO());
        when(mockBoardMapper.toEntity(any(BoardDTO.class))).thenReturn(boardEntity);
        when(mockListCardMapper.toDTO(any(ListCard.class))).thenReturn(listCardDTO);

        // Appel du service
        listCardServiceUnderTest.updateListCard(boardId, dtoList);

        // Vérification des sauvegardes attendues
        verify(mockListCardRepository).saveAll(anyList());
        verify(mockBoardRepository).save(any(Board.class));
    }

    // Helper pour alléger
    private ListCardDTO buildListCardDto(long listCardId, long boardId, String name) {
        ListCardDTO dto = new ListCardDTO();
        dto.setId(listCardId);
        dto.setName(name);
        dto.setOrderIndex(0);
        dto.setIsArchived(false);
        BoardLightDTO board = new BoardLightDTO();
        board.setId(boardId);
        dto.setBoard(board);
        return dto;
    }

    @Test
    void testUpdateListCard2_BoardRepositoryFindByIdReturnsAbsent() {
        // Setup
        final ListCardDTO listCardDTO = new ListCardDTO();
        listCardDTO.setId(0L);
        listCardDTO.setName("name");
        listCardDTO.setOrderIndex(0);
        listCardDTO.setIsArchived(false);
        final BoardLightDTO board = new BoardLightDTO();
        board.setId(0L);
        listCardDTO.setBoard(board);
        final List<ListCardDTO> listCard = List.of(listCardDTO);
        when(mockBoardRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> listCardServiceUnderTest.updateListCard(0L, listCard))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testUpdateListCard2_ListCardRepositoryThrowsOptimisticLockingFailureException() {
        // Préparation des objets communs
        final Long defaultId = 0L;
        final String defaultName = "name";
        final int defaultOrderIndex = 0;
        final boolean defaultIsArchived = false;

        // ListCardDTO de test
        final ListCardDTO expectedListCardDTO = new ListCardDTO();
        expectedListCardDTO.setId(defaultId);
        expectedListCardDTO.setName(defaultName);
        expectedListCardDTO.setOrderIndex(defaultOrderIndex);
        expectedListCardDTO.setIsArchived(defaultIsArchived);

        final BoardLightDTO expectedBoardLightDTO = new BoardLightDTO();
        expectedBoardLightDTO.setId(defaultId);
        expectedListCardDTO.setBoard(expectedBoardLightDTO);
        final List<ListCardDTO> listCardDTOs = List.of(expectedListCardDTO);

        // Board de test pour le mock repository
        final ListCard expectedListCardEntity = ListCard.builder()
                .id(defaultId)
                .name(defaultName)
                .orderIndex(defaultOrderIndex)
                .isArchived(defaultIsArchived)
                .cardList(List.of(Card.builder().build()))
                .build();
        final Board expectedBoard = Board.builder()
                .id(defaultId)
                .cardCount(0)
                .listCards(List.of(expectedListCardEntity))
                .build();
        when(mockBoardRepository.findById(defaultId)).thenReturn(Optional.of(expectedBoard));

        // BoardDTO pour le mock mapper
        final BoardDTO expectedBoardDTO = new BoardDTO();
        final ListCardDTO boardDtoListCard = new ListCardDTO();
        boardDtoListCard.setId(defaultId);
        boardDtoListCard.setName(defaultName);
        boardDtoListCard.setOrderIndex(defaultOrderIndex);
        boardDtoListCard.setIsArchived(defaultIsArchived);

        final BoardLightDTO boardDtoLight = new BoardLightDTO();
        boardDtoLight.setId(defaultId);
        boardDtoListCard.setBoard(boardDtoLight);

        expectedBoardDTO.setListCards(List.of(boardDtoListCard));
        when(mockBoardMapper.toDTO(any(Board.class))).thenReturn(expectedBoardDTO);

        // Mapper DTO -> entity
        when(mockBoardMapper.toEntity(any(BoardDTO.class))).thenReturn(expectedBoard);

        // Préparation de la sauvegarde qui lève l'exception (refactoring clef ici)
        when(mockListCardRepository.saveAll(anyList()))
                .thenThrow(OptimisticLockingFailureException.class);

        // Exécution et vérification
        assertThatThrownBy(() -> listCardServiceUnderTest.updateListCard(defaultId, new ArrayList<>(listCardDTOs)))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    @Test
    void testUpdateListCard2_BoardRepositorySaveThrowsOptimisticLockingFailureException() {
        // Setup des objets de test
        final ListCardDTO listCardDTO = buildListCardDto(0L, 0L, "name");
        final List<ListCardDTO> listCardList = List.of(listCardDTO);
        // Board mocké retourné par le repository
        final Optional<Board> boardOptional = Optional.of(
                createBoardEntity(0L, createListCardEntity(0L, "name"))
        );
        when(mockBoardRepository.findById(0L)).thenReturn(boardOptional);
        // Mock du mapper vers DTO
        final BoardDTO boardDTO = new BoardDTO();
        boardDTO.setListCards(List.of(buildListCardDto(0L, 0L, "name")));
        when(mockBoardMapper.toDTO(any(Board.class))).thenReturn(boardDTO);
        // Mock du mapper vers entity
        when(mockBoardMapper.toEntity(any(BoardDTO.class)))
                .thenReturn(createBoardEntity(0L, createListCardEntity(0L, "name")));
        when(mockBoardRepository.save(any(Board.class))).thenThrow(OptimisticLockingFailureException.class);

        // Exécution & vérification
        assertThatThrownBy(() -> listCardServiceUnderTest.updateListCard(0L, listCardList))
                .isInstanceOf(OptimisticLockingFailureException.class);

        verify(mockListCardRepository).saveAll(
                List.of(createListCardEntity(0L, "name"))
        );
    }



}
