package com.thomas.gestPro.service.unit;

import com.thomas.gestPro.Security.JwtResponse;
import com.thomas.gestPro.Security.JwtTokenUtil;
import com.thomas.gestPro.Security.TokenRequest;
import com.thomas.gestPro.dto.UserDTO;
import com.thomas.gestPro.dto.WorkspaceDTO;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.service.AuthService;
import com.thomas.gestPro.service.TemporaryUserService;
import com.thomas.gestPro.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceTest {

    private static final String XSS_PAYLOAD = "<script>alert('xss')</script>";
    private static final String SQL_INJECTION = "' OR '1'='1";
    private static final String WEAK_PASSWORD = "password123";
    private static final String STRONG_PASSWORD = "StrongP@ss123!";
    private static final int MAX_LOGIN_ATTEMPTS = 3;

    @Mock
    private AuthenticationManager mockAuthenticationManager;
    @Mock
    private JwtTokenUtil mockJwtUtil;
    @Mock
    private UserService mockUserService;
    @Mock
    private TemporaryUserService mockTemporaryUserService;

    private AuthService authServiceUnderTest;

    @BeforeEach
    void setUp() {
        authServiceUnderTest = new AuthService(mockAuthenticationManager, mockJwtUtil, mockUserService,
                mockTemporaryUserService);
    }

    @Test
    void testGenerateTokenAndCreateCookie() {
        // Setup
        final UserDTO user = new UserDTO();
        user.setId(0L);
        user.setUsername("username");
        user.setEmail("email");
        user.setPictureUrl("pictureUrl");
        final WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        user.setWorkspaces(List.of(workspaceDTO));

        when(mockJwtUtil.generateAccessToken("username")).thenReturn("result");
        when(mockJwtUtil.generateRefreshToken("username")).thenReturn("result");

        // Run the test
        final ResponseEntity<JwtResponse> result = authServiceUnderTest.generateTokenAndCreateCookie("username", user);

        // Verify the results
    }

    @Test
    void testAuthenticate() {
        // Setup
        final User user = User.builder()
                .username("username")
                .password("password")
                .build();

        // Configure AuthenticationManager.authenticate(...).
        final Authentication authentication = new TestingAuthenticationToken("user", "pass", "ROLE_USER");
        when(mockAuthenticationManager.authenticate(
                new TestingAuthenticationToken("user", "pass", "ROLE_USER"))).thenReturn(authentication);

        // Configure UserService.getUserByUsername(...).
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(0L);
        userDTO.setUsername("username");
        userDTO.setEmail("email");
        userDTO.setPictureUrl("pictureUrl");
        final WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        userDTO.setWorkspaces(List.of(workspaceDTO));
        when(mockUserService.getUserByUsername("username")).thenReturn(userDTO);

        when(mockJwtUtil.generateAccessToken("username")).thenReturn("result");
        when(mockJwtUtil.generateRefreshToken("username")).thenReturn("result");

        // Run the test
        final ResponseEntity<JwtResponse> result = authServiceUnderTest.authenticate(user);

        // Verify the results
    }
    

    @Test
    void testLogout() {
        // Setup
        when(mockJwtUtil.deleteJwtCookie("refreshToken")).thenReturn(null);

        // Run the test
        final ResponseEntity<JwtResponse> result = authServiceUnderTest.logout();

        // Verify the results
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void testAuthenticateWithXssPayload() {
        final User user = User.builder()
                .username(XSS_PAYLOAD)
                .password(STRONG_PASSWORD)
                .build();

        when(mockAuthenticationManager.authenticate(any()))
                .thenThrow(new AuthenticationException("Invalid characters in username") {
                });

        assertThatThrownBy(() -> authServiceUnderTest.authenticate(user))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    void testAuthenticateWithSqlInjection() {
        final User user = User.builder()
                .username(SQL_INJECTION)
                .password(STRONG_PASSWORD)
                .build();

        when(mockAuthenticationManager.authenticate(any()))
                .thenThrow(new AuthenticationException("Invalid characters in username") {
                });

        assertThatThrownBy(() -> authServiceUnderTest.authenticate(user))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    void testAuthenticateWithWeakPassword() {
        final User user = User.builder()
                .username("validuser")
                .password(WEAK_PASSWORD)
                .build();

        when(mockAuthenticationManager.authenticate(any()))
                .thenThrow(new AuthenticationException("Password does not meet complexity requirements") {
                });

        assertThatThrownBy(() -> authServiceUnderTest.authenticate(user))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    void testBruteForceProtection() {
        final User user = User.builder()
                .username("testuser")
                .password("wrongpass")
                .build();

        when(mockAuthenticationManager.authenticate(any()))
                .thenThrow(new AuthenticationException("Invalid credentials") {
                });

        for (int i = 0; i < MAX_LOGIN_ATTEMPTS; i++) {
            assertThatThrownBy(() -> authServiceUnderTest.authenticate(user))
                    .isInstanceOf(AuthenticationException.class);
        }

        verify(mockAuthenticationManager, times(MAX_LOGIN_ATTEMPTS)).authenticate(any());
    }

    @Test
    void testRefresh() {
        // Setup
        when(mockJwtUtil.getUsernameFromToken("refreshToken")).thenReturn("username");
        when(mockTemporaryUserService.loadUserByUsername("username")).thenReturn(null);
        when(mockJwtUtil.validateToken(eq("refreshToken"), any(UserDetails.class))).thenReturn(false);

        // Run the test
        final ResponseEntity<JwtResponse> result = authServiceUnderTest.refresh("refreshToken");

        // Verify the results
    }

    @Test
    void testRefresh_TemporaryUserServiceThrowsUsernameNotFoundException() {
        // Setup
        when(mockJwtUtil.getUsernameFromToken("refreshToken")).thenReturn("username");
        when(mockTemporaryUserService.loadUserByUsername("username")).thenThrow(UsernameNotFoundException.class);

        // Run the test
        assertThatThrownBy(() -> authServiceUnderTest.refresh("refreshToken"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    void testRefresh_JwtTokenUtilValidateTokenReturnsTrue() {
        // Setup
        when(mockJwtUtil.getUsernameFromToken("refreshToken")).thenReturn("username");
        when(mockTemporaryUserService.loadUserByUsername("username")).thenReturn(null);
        when(mockJwtUtil.validateToken(eq("refreshToken"), any(UserDetails.class))).thenReturn(true);

        // Configure UserService.getUserByUsername(...).
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(0L);
        userDTO.setUsername("username");
        userDTO.setEmail("email");
        userDTO.setPictureUrl("pictureUrl");
        final WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        userDTO.setWorkspaces(List.of(workspaceDTO));
        when(mockUserService.getUserByUsername("username")).thenReturn(userDTO);

        // Run the test
        final ResponseEntity<JwtResponse> result = authServiceUnderTest.refresh("refreshToken");

        // Verify the results
    }

    @Test
    void testAuthenticateOAuth() {
        // Setup
        final TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setToken("token");

        // Configure UserService.createGoogleUser(...).
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(0L);
        userDTO.setUsername("username");
        userDTO.setEmail("email");
        userDTO.setPictureUrl("pictureUrl");
        final WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        userDTO.setWorkspaces(List.of(workspaceDTO));
        when(mockUserService.createGoogleUser("username", "email", "pictureUrl", "subject")).thenReturn(userDTO);

        when(mockJwtUtil.generateAccessToken("username")).thenReturn("result");
        when(mockJwtUtil.generateRefreshToken("username")).thenReturn("result");

        // Run the test
        final ResponseEntity<JwtResponse> result = authServiceUnderTest.authenticateOAuth(tokenRequest);

        // Verify the results
    }

    @Test
    void testGetCurrentUser() {
        // Setup
        final MockHttpServletRequest request = new MockHttpServletRequest();
        when(mockJwtUtil.validateGoogleToken("token")).thenReturn(false);

        // Configure UserService.getUserByUsername(...).
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(0L);
        userDTO.setUsername("username");
        userDTO.setEmail("email");
        userDTO.setPictureUrl("pictureUrl");
        final WorkspaceDTO workspaceDTO = new WorkspaceDTO();
        userDTO.setWorkspaces(List.of(workspaceDTO));
        when(mockUserService.getUserByUsername("username")).thenReturn(userDTO);

        // Run the test
        final ResponseEntity<JwtResponse> result = authServiceUnderTest.getCurrentUser("token", request);

        // Verify the results
    }

    @Test
    void testGetCurrentUser_JwtTokenUtilValidateGoogleTokenReturnsTrue() {
        // Setup
        final MockHttpServletRequest request = new MockHttpServletRequest();
        when(mockJwtUtil.validateGoogleToken("token")).thenReturn(true);
        when(mockJwtUtil.getUsernameFromToken("token")).thenReturn("message");

        // Run the test
        final ResponseEntity<JwtResponse> result = authServiceUnderTest.getCurrentUser("token", request);

        // Verify the results
    }
}
