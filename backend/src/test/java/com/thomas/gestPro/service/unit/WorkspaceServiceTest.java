package com.thomas.gestPro.service.unit;

import com.thomas.gestPro.dto.WorkspaceDTO;
import com.thomas.gestPro.mapper.BoardMapper;
import com.thomas.gestPro.mapper.UserMapper;
import com.thomas.gestPro.mapper.WorkspaceMapper;
import com.thomas.gestPro.model.Board;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.model.Workspace;
import com.thomas.gestPro.repository.BoardRepository;
import com.thomas.gestPro.repository.UserRepository;
import com.thomas.gestPro.repository.WorkspaceRepository;
import com.thomas.gestPro.service.WorkspaceService;
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
class WorkspaceServiceTest {

    @Mock
    private WorkspaceRepository mockWorkspaceRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private BoardRepository mockBoardRepository;
    @Mock
    private WorkspaceMapper mockWorkspaceMapper;
    @Mock
    private BoardMapper mockBoardMapper;
    @Mock
    private UserMapper mockUserMapper;

    private WorkspaceService workspaceServiceUnderTest;

    @BeforeEach
    void setUp() {
        workspaceServiceUnderTest = new WorkspaceService(mockWorkspaceRepository, mockUserRepository,
                mockBoardRepository, mockWorkspaceMapper, mockBoardMapper, mockUserMapper);
    }

    @Test
    void testGetAllWorkspaces() {
        // Setup
        // Configure WorkspaceRepository.findAll(...).
        final List<Workspace> workspaces = List.of(Workspace.builder()
                .name("name")
                .description("description")
                .updateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                .isFavorite(false)
                .boards(List.of(Board.builder()
                        .id(0L)
                        .build()))
                .users(List.of(User.builder().build()))
                .build());
        when(mockWorkspaceRepository.findAll()).thenReturn(workspaces);

        // Run the test
        final List<Workspace> result = workspaceServiceUnderTest.getAllWorkspaces();

        // Verify the results
        assertThat(result).isEqualTo(workspaces);
    }

    @Test
    void testGetAllWorkspaces_WorkspaceRepositoryReturnsNoItems() {
        // Setup
        when(mockWorkspaceRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final List<Workspace> result = workspaceServiceUnderTest.getAllWorkspaces();

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testGetWorkspaceById() {
        // Setup
        // Configure WorkspaceRepository.findById(...).
        final Optional<Workspace> workspace = Optional.of(Workspace.builder()
                .name("name")
                .description("description")
                .updateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                .isFavorite(false)
                .boards(List.of(Board.builder()
                        .id(0L)
                        .build()))
                .users(List.of(User.builder()
                        .workspaces(List.of())
                        .build()))
                .build());
        when(mockWorkspaceRepository.findById(0L)).thenReturn(workspace);

        // Configure WorkspaceMapper.toDTO(...).
        final WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        workspaceDTO.setId(0L);
        workspaceDTO.setName("name");
        workspaceDTO.setDescription("description");
        workspaceDTO.setUpdateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        workspaceDTO.setIsFavorite(false);
        when(mockWorkspaceMapper.toDTO(any(Workspace.class))).thenReturn(workspaceDTO);

        // Run the test
        final WorkspaceDTO result = workspaceServiceUnderTest.getWorkspaceById(0L);

        // Verify the results
        assertThat(result).isEqualTo(workspaceDTO);
    }

    @Test
    void testGetWorkspaceById_WorkspaceRepositoryReturnsAbsent() {
        // Setup
        when(mockWorkspaceRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> workspaceServiceUnderTest.getWorkspaceById(0L)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void testCreateWorkspace() {
        // Setup
        final Workspace workspace = Workspace.builder()
                .name("name")
                .description("description")
                .updateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                .isFavorite(false)
                .boards(new ArrayList<>(List.of(Board.builder()
                        .id(0L)
                        .build())))
                .users(new ArrayList<>(List.of(User.builder()
                        .workspaces(new ArrayList<>())
                        .build())))
                .build();

        // Configure UserRepository.findById(...).
        final Optional<User> user = Optional.of(User.builder()
                .workspaces(new ArrayList<>(List.of(Workspace.builder()
                        .name("name")
                        .description("description")
                        .updateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                        .isFavorite(false)
                        .boards(new ArrayList<>(List.of(Board.builder()
                                .id(0L)
                                .build())))
                        .users(new ArrayList<>())
                        .build())))
                .build());

        when(mockUserRepository.findById(0L)).thenReturn(user);

        // Configure WorkspaceMapper.toDTO(...).
        final WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        workspaceDTO.setId(0L);
        workspaceDTO.setName("name");
        workspaceDTO.setDescription("description");
        workspaceDTO.setUpdateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        workspaceDTO.setIsFavorite(false);
        when(mockWorkspaceMapper.toDTO(any(Workspace.class))).thenReturn(workspaceDTO);

        // Run the test
        final WorkspaceDTO result = workspaceServiceUnderTest.createWorkspace(0L, workspace);

        // Verify the results
        verify(mockWorkspaceRepository).save(any(Workspace.class));
        verify(mockUserRepository).save(any(User.class));
    }


    @Test
    void testCreateWorkspace_UserRepositoryFindByIdReturnsAbsent() {
        // Setup
        final Workspace workspace = Workspace.builder()
                .name("name")
                .description("description")
                .updateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                .isFavorite(false)
                .boards(List.of(Board.builder()
                        .id(0L)
                        .build()))
                .users(List.of(User.builder()
                        .workspaces(List.of())
                        .build()))
                .build();
        when(mockUserRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> workspaceServiceUnderTest.createWorkspace(0L, workspace))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void testCreateWorkspace_WorkspaceRepositoryThrowsOptimisticLockingFailureException() {
        // Setup
        final Workspace workspace = Workspace.builder()
                .name("name")
                .description("description")
                .updateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                .isFavorite(false)
                .boards(List.of(Board.builder()
                        .id(0L)
                        .build()))
                .users(List.of(User.builder()
                        .workspaces(List.of())
                        .build()))
                .build();

        // Configure UserRepository.findById(...).
        final Optional<User> user = Optional.of(User.builder()
                .workspaces(List.of(Workspace.builder()
                        .name("name")
                        .description("description")
                        .updateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                        .isFavorite(false)
                        .boards(List.of(Board.builder()
                                .id(0L)
                                .build()))
                        .users(List.of())
                        .build()))
                .build());
        when(mockUserRepository.findById(0L)).thenReturn(user);

        when(mockWorkspaceRepository.save(any(Workspace.class))).thenThrow(OptimisticLockingFailureException.class);

        // Run the test
        assertThatThrownBy(() -> workspaceServiceUnderTest.createWorkspace(0L, workspace))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    @Test
    void testCreateWorkspace_UserRepositorySaveThrowsOptimisticLockingFailureException() {
        // Setup
        final Workspace workspace = Workspace.builder()
                .name("name")
                .description("description")
                .updateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                .isFavorite(false)
                .boards(new ArrayList<>(List.of(Board.builder()
                        .id(0L)
                        .build())))
                .users(new ArrayList<>(List.of(User.builder()
                        .workspaces(new ArrayList<>())
                        .build())))
                .build();

        // Configure UserRepository.findById(...).
        final Optional<User> user = Optional.of(User.builder()
                .workspaces(new ArrayList<>(List.of(Workspace.builder()
                        .name("name")
                        .description("description")
                        .updateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                        .isFavorite(false)
                        .boards(new ArrayList<>(List.of(Board.builder()
                                .id(0L)
                                .build())))
                        .users(new ArrayList<>())
                        .build())))
                .build());

        when(mockUserRepository.findById(0L)).thenReturn(user);
        when(mockUserRepository.save(any(User.class))).thenThrow(new OptimisticLockingFailureException("Failed to save user due to optimistic locking"));

        // Run the test
        assertThatThrownBy(() -> workspaceServiceUnderTest.createWorkspace(0L, workspace))
                .isInstanceOf(OptimisticLockingFailureException.class)
                .hasMessageContaining("Failed to save user due to optimistic locking");

        // Verify the results
        verify(mockWorkspaceRepository).save(any(Workspace.class));
    }


    @Test
    void testGetWorkspacesByUserId() {
        // Setup
        // Configure UserRepository.findById(...).
        final Optional<User> user = Optional.of(User.builder()
                .workspaces(List.of(Workspace.builder()
                        .name("name")
                        .description("description")
                        .updateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                        .isFavorite(false)
                        .boards(List.of(Board.builder()
                                .id(0L)
                                .build()))
                        .users(List.of())
                        .build()))
                .build());
        when(mockUserRepository.findById(0L)).thenReturn(user);

        // Configure WorkspaceMapper.toDTO(...).
        final WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        workspaceDTO.setId(0L);
        workspaceDTO.setName("name");
        workspaceDTO.setDescription("description");
        workspaceDTO.setUpdateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        workspaceDTO.setIsFavorite(false);
        when(mockWorkspaceMapper.toDTO(any(Workspace.class))).thenReturn(workspaceDTO);

        // Run the test
        final List<WorkspaceDTO> result = workspaceServiceUnderTest.getWorkspacesByUserId(0L);

        // Verify the results
    }

    @Test
    void testGetWorkspacesByUserId_UserRepositoryReturnsAbsent() {
        // Setup
        when(mockUserRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        final List<WorkspaceDTO> result = workspaceServiceUnderTest.getWorkspacesByUserId(0L);

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testUpdateWorkspace() {
        // Setup
        final Workspace updateWorkspace = Workspace.builder()
                .name("name")
                .description("description")
                .updateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                .isFavorite(false)
                .boards(List.of(Board.builder()
                        .id(0L)
                        .build()))
                .users(List.of(User.builder()
                        .workspaces(List.of())
                        .build()))
                .build();

        // Configure WorkspaceRepository.findById(...).
        final Optional<Workspace> workspace = Optional.of(Workspace.builder()
                .name("name")
                .description("description")
                .updateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                .isFavorite(false)
                .boards(List.of(Board.builder()
                        .id(0L)
                        .build()))
                .users(List.of(User.builder()
                        .workspaces(List.of())
                        .build()))
                .build());

        when(mockWorkspaceRepository.findById(0L)).thenReturn(workspace);

        // Configure WorkspaceRepository.save(...).
        final Workspace workspace1 = Workspace.builder()
                .name("name")
                .description("description")
                .updateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                .isFavorite(false)
                .boards(List.of(Board.builder()
                        .id(0L)
                        .build()))
                .users(List.of(User.builder()
                        .workspaces(List.of())
                        .build()))
                .build();
        when(mockWorkspaceRepository.save(any(Workspace.class))).thenReturn(workspace1);

        // Configure WorkspaceMapper.toDTO(...).
        final WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        workspaceDTO.setId(0L);
        workspaceDTO.setName("name");
        workspaceDTO.setDescription("description");
        workspaceDTO.setUpdateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        workspaceDTO.setIsFavorite(false);
        when(mockWorkspaceMapper.toDTO(any(Workspace.class))).thenReturn(workspaceDTO);

        // Run the test
        final WorkspaceDTO result = workspaceServiceUnderTest.updateWorkspace(0L, updateWorkspace);

        // Verify the results
    }

    @Test
    void testUpdateWorkspace_WorkspaceRepositoryFindByIdReturnsAbsent() {
        // Setup
        final Workspace updateWorkspace = Workspace.builder()
                .name("name")
                .description("description")
                .updateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                .isFavorite(false)
                .boards(List.of(Board.builder()
                        .id(0L)
                        .build()))
                .users(List.of(User.builder()
                        .workspaces(List.of())
                        .build()))
                .build();
        when(mockWorkspaceRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> workspaceServiceUnderTest.updateWorkspace(0L, updateWorkspace))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void testUpdateWorkspace_WorkspaceRepositorySaveThrowsOptimisticLockingFailureException() {
        // Setup
        final Workspace updateWorkspace = Workspace.builder()
                .name("name")
                .description("description")
                .updateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                .isFavorite(false)
                .boards(List.of(Board.builder()
                        .id(0L)
                        .build()))
                .users(List.of(User.builder()
                        .workspaces(List.of())
                        .build()))
                .build();

        // Configure WorkspaceRepository.findById(...).
        final Optional<Workspace> workspace = Optional.of(Workspace.builder()
                .name("name")
                .description("description")
                .updateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                .isFavorite(false)
                .boards(List.of(Board.builder()
                        .id(0L)
                        .build()))
                .users(List.of(User.builder()
                        .workspaces(List.of())
                        .build()))
                .build());
        when(mockWorkspaceRepository.findById(0L)).thenReturn(workspace);

        when(mockWorkspaceRepository.save(any(Workspace.class))).thenThrow(OptimisticLockingFailureException.class);

        // Run the test
        assertThatThrownBy(() -> workspaceServiceUnderTest.updateWorkspace(0L, updateWorkspace))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    @Test
    void testDeleteWorkspace() {
        // Setup
        // Run the test
        workspaceServiceUnderTest.deleteWorkspace(0L);

        // Verify the results
        verify(mockWorkspaceRepository).deleteById(0L);
    }

    @Test
    void testGetWorkspacesByBoardId() {
        // Setup
        // Configure WorkspaceRepository.findAll(...).
        final List<Workspace> workspaces = List.of(Workspace.builder()
                .name("name")
                .description("description")
                .updateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime())
                .isFavorite(false)
                .boards(List.of(Board.builder()
                        .id(0L)
                        .build()))
                .users(List.of(User.builder().build()))
                .build());
        when(mockWorkspaceRepository.findAll()).thenReturn(workspaces);

        // Configure WorkspaceMapper.toDTO(...).
        final WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        workspaceDTO.setId(0L);
        workspaceDTO.setName("name");
        workspaceDTO.setDescription("description");
        workspaceDTO.setUpdateAt(new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime());
        workspaceDTO.setIsFavorite(false);
        when(mockWorkspaceMapper.toDTO(any(Workspace.class))).thenReturn(workspaceDTO);

        // Run the test
        final WorkspaceDTO result = workspaceServiceUnderTest.getWorkspacesByBoardId(0L);

        // Verify the results
    }

    @Test
    void testGetWorkspacesByBoardId_WorkspaceRepositoryReturnsNoItems() {
        // Setup
        when(mockWorkspaceRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        assertThatThrownBy(() -> workspaceServiceUnderTest.getWorkspacesByBoardId(0L))
                .isInstanceOf(RuntimeException.class);
    }
}
