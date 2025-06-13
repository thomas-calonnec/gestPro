package com.thomas.gestPro.service.unit;

import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.dto.BoardDTO;
import com.thomas.gestPro.dto.ListCardDTO;
import com.thomas.gestPro.dto.UserLightDTO;
import com.thomas.gestPro.mapper.BoardMapper;
import com.thomas.gestPro.mapper.ListCardMapper;
import com.thomas.gestPro.model.Board;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.model.Workspace;
import com.thomas.gestPro.repository.BoardRepository;
import com.thomas.gestPro.repository.ListCardRepository;
import com.thomas.gestPro.repository.UserRepository;
import com.thomas.gestPro.repository.WorkspaceRepository;
import com.thomas.gestPro.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository mockBoardRepository;
    @Mock
    private ListCardRepository mockListCardRepository;
    @Mock
    private WorkspaceRepository mockWorkspaceRepository;
    @Mock
    private ListCardMapper mockListCardMapper;
    @Mock
    private BoardMapper mockBoardMapper;
    @Mock
    private UserRepository mockUserRepository;

    private BoardService boardServiceUnderTest;

    @BeforeEach
    void setUp() {
        boardServiceUnderTest = new BoardService(mockBoardRepository, mockListCardRepository, mockWorkspaceRepository,
                mockListCardMapper, mockBoardMapper, mockUserRepository);
    }

    @Test
    void testGetBoardById() {
        // Setup
        // Configure BoardRepository.findById(...).
        final Optional<Board> board = Optional.of(Board.builder()
                .name("name")
                .color("color")
                .description("description")
                .lastUpdated(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                .cardCount(0)
                .status("status")
                .ownerId(0L)
                .members(List.of(User.builder()
                        .boards(List.of())
                        .build()))
                .workspaces(List.of(Workspace.builder()
                        .boards(List.of())
                        .build()))
                .build());
        when(mockBoardRepository.findById(0L)).thenReturn(board);

        // Configure BoardMapper.toDTO(...).
        final BoardDTO boardDTO = new BoardDTO();
        boardDTO.setName("name");
        boardDTO.setDescription("description");
        boardDTO.setStatus("status");
        boardDTO.setOwnerId(0L);
        final ListCardDTO listCardDTO = new ListCardDTO();
        boardDTO.setListCards(List.of(listCardDTO));
        final UserLightDTO userLightDTO = new UserLightDTO();
        userLightDTO.setId(0L);
        userLightDTO.setUsername("username");
        boardDTO.setMembers(List.of(userLightDTO));
        when(mockBoardMapper.toDTO(any(Board.class))).thenReturn(boardDTO);

        // Run the test
        final BoardDTO result = boardServiceUnderTest.getBoardById(0L);

        // Verify the results
    }

    @Test
    void testGetBoardById_BoardRepositoryReturnsAbsent() {
        // Setup
        when(mockBoardRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> boardServiceUnderTest.getBoardById(0L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testGetCardsByBoardId() {
        // Setup
        // Configure BoardRepository.findById(...).
        final Optional<Board> board = Optional.of(Board.builder()
                .name("name")
                .color("color")
                .description("description")
                .lastUpdated(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                .cardCount(0)
                .status("status")
                .ownerId(0L)
                .members(List.of(User.builder()
                        .boards(List.of())
                        .build()))
                .workspaces(List.of(Workspace.builder()
                        .boards(List.of())
                        .build()))
                .build());
        when(mockBoardRepository.findById(0L)).thenReturn(board);

        // Configure BoardMapper.toDTO(...).
        final BoardDTO boardDTO = new BoardDTO();
        boardDTO.setName("name");
        boardDTO.setDescription("description");
        boardDTO.setStatus("status");
        boardDTO.setOwnerId(0L);
        final ListCardDTO listCardDTO = new ListCardDTO();
        boardDTO.setListCards(List.of(listCardDTO));
        final UserLightDTO userLightDTO = new UserLightDTO();
        userLightDTO.setId(0L);
        userLightDTO.setUsername("username");
        boardDTO.setMembers(List.of(userLightDTO));
        when(mockBoardMapper.toDTO(any(Board.class))).thenReturn(boardDTO);

        // Run the test
        final List<ListCardDTO> result = boardServiceUnderTest.getCardsByBoardId(0L);

        // Verify the results
    }

    @Test
    void testGetCardsByBoardId_BoardRepositoryReturnsAbsent() {
        // Setup
        when(mockBoardRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> boardServiceUnderTest.getCardsByBoardId(0L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testGetListBoardByWorkspaceId() {
        // Setup
        // Configure WorkspaceRepository.findById(...).
        final Optional<Workspace> workspace = Optional.of(Workspace.builder()
                .boards(List.of(Board.builder()
                        .name("name")
                        .color("color")
                        .description("description")
                        .lastUpdated(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                        .cardCount(0)
                        .status("status")
                        .ownerId(0L)
                        .members(List.of(User.builder().build()))
                        .workspaces(List.of())
                        .build()))
                .build());
        when(mockWorkspaceRepository.findById(0L)).thenReturn(workspace);

        // Configure BoardMapper.toDTO(...).
        final BoardDTO boardDTO = new BoardDTO();
        boardDTO.setName("name");
        boardDTO.setDescription("description");
        boardDTO.setStatus("status");
        boardDTO.setOwnerId(0L);
        final ListCardDTO listCardDTO = new ListCardDTO();
        boardDTO.setListCards(List.of(listCardDTO));
        final UserLightDTO userLightDTO = new UserLightDTO();
        userLightDTO.setId(0L);
        userLightDTO.setUsername("username");
        boardDTO.setMembers(List.of(userLightDTO));
        when(mockBoardMapper.toDTO(any(Board.class))).thenReturn(boardDTO);

        // Run the test
        final List<BoardDTO> result = boardServiceUnderTest.getListBoardByWorkspaceId(0L);

        // Verify the results
    }

    @Test
    void testGetListBoardByWorkspaceId_WorkspaceRepositoryReturnsAbsent() {
        // Setup
        when(mockWorkspaceRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> boardServiceUnderTest.getListBoardByWorkspaceId(0L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void testCreateBoard() {
        // Setup
        final BoardDTO boardInput = new BoardDTO();
        boardInput.setName("name");
        boardInput.setDescription("description");
        boardInput.setStatus("status");
        boardInput.setOwnerId(0L);
        final ListCardDTO listCardDTO = new ListCardDTO();
        boardInput.setListCards(new ArrayList<>(List.of(listCardDTO)));
        final UserLightDTO userLightDTO = new UserLightDTO();
        userLightDTO.setId(0L);
        userLightDTO.setUsername("username");
        boardInput.setMembers(new ArrayList<>(List.of(userLightDTO)));

        // Configure WorkspaceRepository.findById(...).
        final Optional<Workspace> workspace = Optional.of(Workspace.builder()
                .boards(new ArrayList<>(List.of(Board.builder()
                        .name("name")
                        .color("color")
                        .description("description")
                        .lastUpdated(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                        .cardCount(0)
                        .status("status")
                        .ownerId(0L)
                        .members(new ArrayList<>(List.of(User.builder().build())))
                        .workspaces(new ArrayList<>(List.of()))
                        .build())))
                .build());
        when(mockWorkspaceRepository.findById(0L)).thenReturn(workspace);

        // Configure UserRepository.findById(...).
        final Optional<User> user = Optional.of(User.builder()
                .boards(new ArrayList<>(List.of(Board.builder()
                        .name("name")
                        .color("color")
                        .description("description")
                        .lastUpdated(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                        .cardCount(0)
                        .status("status")
                        .ownerId(0L)
                        .members(new ArrayList<>(List.of()))
                        .workspaces(new ArrayList<>(List.of(Workspace.builder().build())))
                        .build())))
                .build());
        when(mockUserRepository.findById(0L)).thenReturn(user);

        // Configure BoardMapper.toDTO(...).
        final BoardDTO boardDTO = new BoardDTO();
        boardDTO.setName("name");
        boardDTO.setDescription("description");
        boardDTO.setStatus("status");
        boardDTO.setOwnerId(0L);
        final ListCardDTO listCardDTO1 = new ListCardDTO();
        boardDTO.setListCards(new ArrayList<>(List.of(listCardDTO1)));
        final UserLightDTO userLightDTO1 = new UserLightDTO();
        userLightDTO1.setId(0L);
        userLightDTO1.setUsername("username");
        boardDTO.setMembers(new ArrayList<>(List.of(userLightDTO1)));
        when(mockBoardMapper.toDTO(any(Board.class))).thenReturn(boardDTO);

        // Run the test
        final BoardDTO result = boardServiceUnderTest.createBoard(0L, boardInput);

        // Verify the results
        verify(mockBoardRepository).save(any(Board.class));
    }

    @Test
    void testCreateBoard_WorkspaceRepositoryReturnsAbsent() {
        // Setup
        final BoardDTO boardInput = new BoardDTO();
        boardInput.setName("name");
        boardInput.setDescription("description");
        boardInput.setStatus("status");
        boardInput.setOwnerId(0L);
        final ListCardDTO listCardDTO = new ListCardDTO();
        boardInput.setListCards(List.of(listCardDTO));
        final UserLightDTO userLightDTO = new UserLightDTO();
        userLightDTO.setId(0L);
        userLightDTO.setUsername("username");
        boardInput.setMembers(List.of(userLightDTO));

        when(mockWorkspaceRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> boardServiceUnderTest.createBoard(0L, boardInput))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void testCreateBoard_UserRepositoryReturnsAbsent() {
        // Setup
        final BoardDTO boardInput = new BoardDTO();
        boardInput.setName("name");
        boardInput.setDescription("description");
        boardInput.setStatus("status");
        boardInput.setOwnerId(0L);
        final ListCardDTO listCardDTO = new ListCardDTO();
        boardInput.setListCards(List.of(listCardDTO));
        final UserLightDTO userLightDTO = new UserLightDTO();
        userLightDTO.setId(0L);
        userLightDTO.setUsername("username");
        boardInput.setMembers(List.of(userLightDTO));

        // Configure WorkspaceRepository.findById(...).
        final Optional<Workspace> workspace = Optional.of(Workspace.builder()
                .boards(List.of(Board.builder()
                        .name("name")
                        .color("color")
                        .description("description")
                        .lastUpdated(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                        .cardCount(0)
                        .status("status")
                        .ownerId(0L)
                        .members(List.of(User.builder().build()))
                        .workspaces(List.of())
                        .build()))
                .build());
        when(mockWorkspaceRepository.findById(0L)).thenReturn(workspace);

        when(mockUserRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> boardServiceUnderTest.createBoard(0L, boardInput))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void testCreateBoard_BoardRepositoryThrowsOptimisticLockingFailureException() {
        // Setup
        final BoardDTO boardInput = new BoardDTO();
        boardInput.setName("name");
        boardInput.setDescription("description");
        boardInput.setStatus("status");
        boardInput.setOwnerId(0L);
        final ListCardDTO listCardDTO = new ListCardDTO();
        boardInput.setListCards(new ArrayList<>(List.of(listCardDTO)));
        final UserLightDTO userLightDTO = new UserLightDTO();
        userLightDTO.setId(0L);
        userLightDTO.setUsername("username");
        boardInput.setMembers(new ArrayList<>(List.of(userLightDTO)));

        // Configure WorkspaceRepository.findById(...).
        final Optional<Workspace> workspace = Optional.of(Workspace.builder()
                .boards(new ArrayList<>(List.of(Board.builder()
                        .name("name")
                        .color("color")
                        .description("description")
                        .lastUpdated(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                        .cardCount(0)
                        .status("status")
                        .ownerId(0L)
                        .members(new ArrayList<>(List.of(User.builder().build())))
                        .workspaces(new ArrayList<>(List.of()))
                        .build())))
                .build());
        when(mockWorkspaceRepository.findById(0L)).thenReturn(workspace);

        // Configure UserRepository.findById(...).
        final Optional<User> user = Optional.of(User.builder()
                .boards(new ArrayList<>(List.of(Board.builder()
                        .name("name")
                        .color("color")
                        .description("description")
                        .lastUpdated(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                        .cardCount(0)
                        .status("status")
                        .ownerId(0L)
                        .members(new ArrayList<>(List.of()))
                        .workspaces(new ArrayList<>(List.of(Workspace.builder().build())))
                        .build())))
                .build());
        when(mockUserRepository.findById(0L)).thenReturn(user);

        when(mockBoardRepository.save(any(Board.class))).thenThrow(OptimisticLockingFailureException.class);

        // Run the test
        assertThatThrownBy(() -> boardServiceUnderTest.createBoard(0L, boardInput))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    @Test
    void testUpdateBoard() {
        // Setup
        final BoardDTO boardInput = new BoardDTO();
        boardInput.setName("name");
        boardInput.setDescription("description");
        boardInput.setStatus("status");
        boardInput.setOwnerId(0L);
        final ListCardDTO listCardDTO = new ListCardDTO();
        boardInput.setListCards(new ArrayList<>(List.of(listCardDTO)));
        final UserLightDTO userLightDTO = new UserLightDTO();
        userLightDTO.setId(0L);
        userLightDTO.setUsername("username");
        boardInput.setMembers(new ArrayList<>(List.of(userLightDTO)));

        // Configure UserRepository.getUserByUsername(...).
        final Optional<User> user = Optional.of(User.builder()
                .boards(new ArrayList<>(List.of(Board.builder()
                        .name("name")
                        .color("color")
                        .description("description")
                        .lastUpdated(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                        .cardCount(0)
                        .status("status")
                        .ownerId(0L)
                        .members(new ArrayList<>(List.of()))
                        .workspaces(new ArrayList<>(List.of(Workspace.builder().build())))
                        .build())))
                .build());
        when(mockUserRepository.getUserByUsername("username")).thenReturn(user);

        // Configure BoardRepository.findById(...).
        final Optional<Board> board = Optional.of(Board.builder()
                .name("name")
                .color("color")
                .description("description")
                .lastUpdated(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                .cardCount(0)
                .status("status")
                .ownerId(0L)
                .members(new ArrayList<>(List.of(User.builder()
                        .boards(new ArrayList<>(List.of()))
                        .build())))
                .workspaces(new ArrayList<>(List.of(Workspace.builder()
                        .boards(new ArrayList<>(List.of()))
                        .build())))
                .build());
        when(mockBoardRepository.findById(0L)).thenReturn(board);

        // Configure BoardMapper.toDTO(...).
        final BoardDTO boardDTO = new BoardDTO();
        boardDTO.setName("name");
        boardDTO.setDescription("description");
        boardDTO.setStatus("status");
        boardDTO.setOwnerId(0L);
        final ListCardDTO listCardDTO1 = new ListCardDTO();
        boardDTO.setListCards(new ArrayList<>(List.of(listCardDTO1)));
        final UserLightDTO userLightDTO1 = new UserLightDTO();
        userLightDTO1.setId(0L);
        userLightDTO1.setUsername("username");
        boardDTO.setMembers(new ArrayList<>(List.of(userLightDTO1)));
        when(mockBoardMapper.toDTO(any(Board.class))).thenReturn(boardDTO);

        // Run the test
        final BoardDTO result = boardServiceUnderTest.updateBoard(0L, boardInput);

        // Verify the results
        verify(mockBoardRepository).save(any(Board.class));
    }

    @Test
    void testUpdateBoard_UserRepositoryReturnsAbsent() {
        // Setup
        final BoardDTO boardInput = new BoardDTO();
        boardInput.setName("name");
        boardInput.setDescription("description");
        boardInput.setStatus("status");
        boardInput.setOwnerId(0L);
        final ListCardDTO listCardDTO = new ListCardDTO();
        boardInput.setListCards(List.of(listCardDTO));
        final UserLightDTO userLightDTO = new UserLightDTO();
        userLightDTO.setId(0L);
        userLightDTO.setUsername("username");
        boardInput.setMembers(List.of(userLightDTO));

        when(mockUserRepository.getUserByUsername("username")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> boardServiceUnderTest.updateBoard(0L, boardInput))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void testUpdateBoard_BoardRepositoryFindByIdReturnsAbsent() {
        // Setup
        final BoardDTO boardInput = new BoardDTO();
        boardInput.setName("name");
        boardInput.setDescription("description");
        boardInput.setStatus("status");
        boardInput.setOwnerId(0L);
        final ListCardDTO listCardDTO = new ListCardDTO();
        boardInput.setListCards(List.of(listCardDTO));
        final UserLightDTO userLightDTO = new UserLightDTO();
        userLightDTO.setId(0L);
        userLightDTO.setUsername("username");
        boardInput.setMembers(List.of(userLightDTO));

        // Configure UserRepository.getUserByUsername(...).
        final Optional<User> user = Optional.of(User.builder()
                .boards(List.of(Board.builder()
                        .name("name")
                        .color("color")
                        .description("description")
                        .lastUpdated(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                        .cardCount(0)
                        .status("status")
                        .ownerId(0L)
                        .members(List.of())
                        .workspaces(List.of(Workspace.builder().build()))
                        .build()))
                .build());
        when(mockUserRepository.getUserByUsername("username")).thenReturn(user);

        when(mockBoardRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> boardServiceUnderTest.updateBoard(0L, boardInput))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void testUpdateBoard_BoardRepositorySaveThrowsOptimisticLockingFailureException() {
        // Setup
        final BoardDTO boardInput = new BoardDTO();
        boardInput.setName("name");
        boardInput.setDescription("description");
        boardInput.setStatus("status");
        boardInput.setOwnerId(0L);
        final ListCardDTO listCardDTO = new ListCardDTO();
        boardInput.setListCards(new ArrayList<>(List.of(listCardDTO)));
        final UserLightDTO userLightDTO = new UserLightDTO();
        userLightDTO.setId(0L);
        userLightDTO.setUsername("username");
        boardInput.setMembers(new ArrayList<>(List.of(userLightDTO)));

        // Configure UserRepository.getUserByUsername(...).
        final Optional<User> user = Optional.of(User.builder()
                .boards(new ArrayList<>(List.of(Board.builder()
                        .name("name")
                        .color("color")
                        .description("description")
                        .lastUpdated(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                        .cardCount(0)
                        .status("status")
                        .ownerId(0L)
                        .members(new ArrayList<>(List.of()))
                        .workspaces(new ArrayList<>(List.of(Workspace.builder().build())))
                        .build())))
                .build());
        when(mockUserRepository.getUserByUsername("username")).thenReturn(user);

        // Configure BoardRepository.findById(...).
        final Optional<Board> board = Optional.of(Board.builder()
                .name("name")
                .color("color")
                .description("description")
                .lastUpdated(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                .cardCount(0)
                .status("status")
                .ownerId(0L)
                .members(new ArrayList<>(List.of(User.builder()
                        .boards(new ArrayList<>(List.of()))
                        .build())))
                .workspaces(new ArrayList<>(List.of(Workspace.builder()
                        .boards(new ArrayList<>(List.of()))
                        .build())))
                .build());
        when(mockBoardRepository.findById(0L)).thenReturn(board);

        when(mockBoardRepository.save(any(Board.class))).thenThrow(OptimisticLockingFailureException.class);

        // Run the test
        assertThatThrownBy(() -> boardServiceUnderTest.updateBoard(0L, boardInput))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    @Test
    void testDeleteBoard() {
        // Setup
        // Run the test
        boardServiceUnderTest.deleteBoard(0L);

        // Verify the results
        verify(mockBoardRepository).deleteById(0L);
    }

    @Test
    void testGetListCardRepository() {
        assertThat(boardServiceUnderTest.getListCardRepository()).isEqualTo(mockListCardRepository);
    }

    @Test
    void testGetListCardMapper() {
        assertThat(boardServiceUnderTest.getListCardMapper()).isEqualTo(mockListCardMapper);
    }
}
