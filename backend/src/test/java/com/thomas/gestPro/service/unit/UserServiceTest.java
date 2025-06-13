package com.thomas.gestPro.service.unit;

import com.thomas.gestPro.dto.UserDTO;
import com.thomas.gestPro.dto.WorkspaceDTO;
import com.thomas.gestPro.mapper.UserMapper;
import com.thomas.gestPro.model.Role;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.repository.RoleRepository;
import com.thomas.gestPro.repository.UserRepository;
import com.thomas.gestPro.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private UserMapper mockUserMapper;
    @Mock
    private RoleRepository mockRoleRepository;

    private UserService userServiceUnderTest;

    @BeforeEach
    void setUp() {
        userServiceUnderTest = new UserService(mockUserRepository, mockUserMapper, mockRoleRepository);
    }

    @Test
    void testGetById() {
        // Setup
        // Configure UserRepository.findById(...).
        final Optional<User> user = Optional.of(User.builder()
                .username("username")
                .password("password")
                .email("email")
                .pictureUrl("pictureUrl")
                .roles(List.of(Role.builder().build()))
                .build());
        when(mockUserRepository.findById(0L)).thenReturn(user);

        // Configure UserMapper.toDTO(...).
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(0L);
        userDTO.setUsername("username");
        userDTO.setEmail("email");
        userDTO.setPictureUrl("pictureUrl");
        final WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        userDTO.setWorkspaces(List.of(workspaceDTO));
        when(mockUserMapper.toDTO(any(User.class))).thenReturn(userDTO);

        // Run the test
        final UserDTO result = userServiceUnderTest.getById(0L);

        // Verify the results
    }

    @Test
    void testGetById_UserRepositoryReturnsAbsent() {
        // Setup
        when(mockUserRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> userServiceUnderTest.getById(0L)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void testGetAllUser() {
        // Setup
        // Configure UserRepository.findAll(...).
        final List<User> users = List.of(User.builder()
                .username("username")
                .password("password")
                .email("email")
                .pictureUrl("pictureUrl")
                .roles(List.of(Role.builder().build()))
                .build());
        when(mockUserRepository.findAll()).thenReturn(users);

        // Configure UserMapper.toDTO(...).
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(0L);
        userDTO.setUsername("username");
        userDTO.setEmail("email");
        userDTO.setPictureUrl("pictureUrl");
        final WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        userDTO.setWorkspaces(List.of(workspaceDTO));
        when(mockUserMapper.toDTO(any(User.class))).thenReturn(userDTO);

        // Run the test
        final List<UserDTO> result = userServiceUnderTest.getAllUser();

        // Verify the results
    }

    @Test
    void testGetAllUser_UserRepositoryReturnsNoItems() {
        // Setup
        when(mockUserRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final List<UserDTO> result = userServiceUnderTest.getAllUser();

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testCreateUserGithub() {
        // Setup
        final User incomingUser = User.builder()
                .username("username")
                .password("password")
                .email("email")
                .pictureUrl("pictureUrl")
                .roles(List.of(Role.builder().build()))
                .build();

        // Configure UserRepository.findByEmail(...).
        final Optional<User> user = Optional.of(User.builder()
                .username("username")
                .password("password")
                .email("email")
                .pictureUrl("pictureUrl")
                .roles(List.of(Role.builder().build()))
                .build());
        when(mockUserRepository.findByEmail("email")).thenReturn(user);

        // Configure UserMapper.toDTO(...).
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(0L);
        userDTO.setUsername("username");
        userDTO.setEmail("email");
        userDTO.setPictureUrl("pictureUrl");
        final WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        userDTO.setWorkspaces(List.of(workspaceDTO));
        when(mockUserMapper.toDTO(any(User.class))).thenReturn(userDTO);

        // Run the test
        final UserDTO result = userServiceUnderTest.createUserGithub(incomingUser);

        // Verify the results
    }

    @Test
    void testCreateUserGithub_UserRepositoryFindByEmailReturnsAbsent() {
        // Setup
        final User incomingUser = User.builder()
                .username("username")
                .password("password")
                .email("email")
                .pictureUrl("pictureUrl")
                .roles(List.of(Role.builder().build()))
                .build();
        when(mockUserRepository.findByEmail("email")).thenReturn(Optional.empty());

        // Configure UserMapper.toDTO(...).
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(0L);
        userDTO.setUsername("username");
        userDTO.setEmail("email");
        userDTO.setPictureUrl("pictureUrl");
        final WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        userDTO.setWorkspaces(List.of(workspaceDTO));
        when(mockUserMapper.toDTO(any(User.class))).thenReturn(userDTO);

        // Run the test
        final UserDTO result = userServiceUnderTest.createUserGithub(incomingUser);

        // Verify the results
        verify(mockUserRepository).save(any(User.class));
    }

    @Test
    void testCreateUserGithub_UserRepositorySaveThrowsOptimisticLockingFailureException() {
        // Setup
        final User incomingUser = User.builder()
                .username("username")
                .password("password")
                .email("email")
                .pictureUrl("pictureUrl")
                .roles(List.of(Role.builder().build()))
                .build();
        when(mockUserRepository.findByEmail("email")).thenReturn(Optional.empty());
        when(mockUserRepository.save(any(User.class))).thenThrow(OptimisticLockingFailureException.class);

        // Run the test
        assertThatThrownBy(() -> userServiceUnderTest.createUserGithub(incomingUser))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    @Test
    void testUpdateUser() {
        // Setup
        final User updateUser = User.builder()
                .username("username")
                .password("password")
                .email("email")
                .pictureUrl("pictureUrl")
                .roles(List.of(Role.builder().build()))
                .build();

        // Configure UserRepository.findById(...).
        final Optional<User> user = Optional.of(User.builder()
                .username("username")
                .password("password")
                .email("email")
                .pictureUrl("pictureUrl")
                .roles(List.of(Role.builder().build()))
                .build());
        when(mockUserRepository.findById(0L)).thenReturn(user);

        // Configure UserMapper.toDTO(...).
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(0L);
        userDTO.setUsername("username");
        userDTO.setEmail("email");
        userDTO.setPictureUrl("pictureUrl");
        final WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        userDTO.setWorkspaces(List.of(workspaceDTO));
        when(mockUserMapper.toDTO(any(User.class))).thenReturn(userDTO);

        // Configure UserMapper.toEntity(...).
        final User user1 = User.builder()
                .username("username")
                .password("password")
                .email("email")
                .pictureUrl("pictureUrl")
                .roles(List.of(Role.builder().build()))
                .build();
        when(mockUserMapper.toEntity(any(UserDTO.class))).thenReturn(user1);

        // Run the test
        final UserDTO result = userServiceUnderTest.updateUser(0L, updateUser);

        // Verify the results
        verify(mockUserRepository).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserRepositoryFindByIdReturnsAbsent() {
        // Setup
        final User updateUser = User.builder()
                .username("username")
                .password("password")
                .email("email")
                .pictureUrl("pictureUrl")
                .roles(List.of(Role.builder().build()))
                .build();
        when(mockUserRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> userServiceUnderTest.updateUser(0L, updateUser))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void testUpdateUser_UserRepositorySaveThrowsOptimisticLockingFailureException() {
        // Setup
        final User updateUser = User.builder()
                .username("username")
                .password("password")
                .email("email")
                .pictureUrl("pictureUrl")
                .roles(List.of(Role.builder().build()))
                .build();

        // Configure UserRepository.findById(...).
        final Optional<User> user = Optional.of(User.builder()
                .username("username")
                .password("password")
                .email("email")
                .pictureUrl("pictureUrl")
                .roles(List.of(Role.builder().build()))
                .build());
        when(mockUserRepository.findById(0L)).thenReturn(user);

        // Configure UserMapper.toDTO(...).
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(0L);
        userDTO.setUsername("username");
        userDTO.setEmail("email");
        userDTO.setPictureUrl("pictureUrl");
        final WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        userDTO.setWorkspaces(List.of(workspaceDTO));
        when(mockUserMapper.toDTO(any(User.class))).thenReturn(userDTO);

        // Configure UserMapper.toEntity(...).
        final User user1 = User.builder()
                .username("username")
                .password("password")
                .email("email")
                .pictureUrl("pictureUrl")
                .roles(List.of(Role.builder().build()))
                .build();
        when(mockUserMapper.toEntity(any(UserDTO.class))).thenReturn(user1);

        when(mockUserRepository.save(any(User.class))).thenThrow(OptimisticLockingFailureException.class);

        // Run the test
        assertThatThrownBy(() -> userServiceUnderTest.updateUser(0L, updateUser))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    @Test
    void testDeleteUser() {
        // Setup
        // Run the test
        userServiceUnderTest.deleteUser(0L);

        // Verify the results
        verify(mockUserRepository).deleteById(0L);
    }

    @Test
    void testGetUserByUsername() {
        // Setup
        // Configure UserRepository.getUserByUsername(...).
        final Optional<User> user = Optional.of(User.builder()
                .username("username")
                .password("password")
                .email("email")
                .pictureUrl("pictureUrl")
                .roles(List.of(Role.builder().build()))
                .build());
        when(mockUserRepository.getUserByUsername("username")).thenReturn(user);

        // Configure UserMapper.toDTO(...).
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(0L);
        userDTO.setUsername("username");
        userDTO.setEmail("email");
        userDTO.setPictureUrl("pictureUrl");
        final WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        userDTO.setWorkspaces(List.of(workspaceDTO));
        when(mockUserMapper.toDTO(any(User.class))).thenReturn(userDTO);

        // Run the test
        final UserDTO result = userServiceUnderTest.getUserByUsername("username");

        // Verify the results
    }

    @Test
    void testGetUserByUsername_UserRepositoryReturnsAbsent() {
        // Setup
        when(mockUserRepository.getUserByUsername("username")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> userServiceUnderTest.getUserByUsername("username"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void testCreateGoogleUser() {
        // Setup
        // Configure UserRepository.findByEmail(...).
        final Optional<User> user = Optional.of(User.builder()
                .username("username")
                .password("password")
                .email("email")
                .pictureUrl("pictureUrl")
                .roles(List.of(Role.builder().build()))
                .build());
        when(mockUserRepository.findByEmail("email")).thenReturn(user);

        // Configure UserMapper.toDTO(...).
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(0L);
        userDTO.setUsername("username");
        userDTO.setEmail("email");
        userDTO.setPictureUrl("pictureUrl");
        final WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        userDTO.setWorkspaces(List.of(workspaceDTO));
        when(mockUserMapper.toDTO(any(User.class))).thenReturn(userDTO);

        // Run the test
        final UserDTO result = userServiceUnderTest.createGoogleUser("username", "email", "pictureUrl", "googleId");

        // Verify the results
    }

    @Test
    void testCreateGoogleUser_UserRepositoryFindByEmailReturnsAbsent() {
        // Setup
        when(mockUserRepository.findByEmail("email")).thenReturn(Optional.empty());

        // Configure UserMapper.toDTO(...).
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(0L);
        userDTO.setUsername("username");
        userDTO.setEmail("email");
        userDTO.setPictureUrl("pictureUrl");
        final WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        userDTO.setWorkspaces(List.of(workspaceDTO));
        when(mockUserMapper.toDTO(any(User.class))).thenReturn(userDTO);

        when(mockRoleRepository.findByName("ROLE_USER")).thenReturn(Role.builder().build());

        // Run the test
        final UserDTO result = userServiceUnderTest.createGoogleUser("username", "email", "pictureUrl", "googleId");

        // Verify the results
        verify(mockUserRepository).save(any(User.class));
    }

    @Test
    void testCreateGoogleUser_UserRepositorySaveThrowsOptimisticLockingFailureException() {
        // Setup
        when(mockUserRepository.findByEmail("email")).thenReturn(Optional.empty());
        when(mockRoleRepository.findByName("ROLE_USER")).thenReturn(Role.builder().build());
        when(mockUserRepository.save(any(User.class))).thenThrow(OptimisticLockingFailureException.class);

        // Run the test
        assertThatThrownBy(() -> userServiceUnderTest.createGoogleUser("username", "email", "pictureUrl",
                "googleId")).isInstanceOf(OptimisticLockingFailureException.class);
    }
}
