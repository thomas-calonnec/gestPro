package com.thomas.gestPro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thomas.gestPro.dto.LoginRequest;
import com.thomas.gestPro.dto.RegisterRequest;
import com.thomas.gestPro.repository.UserRepository;
import com.thomas.gestPro.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    private RegisterRequest validRegisterRequest;
    private LoginRequest validLoginRequest;

    @BeforeEach
    void setUp() {
        validRegisterRequest = RegisterRequest.builder()
                .email("test@example.com")
                .password("Test123!@#")
                .firstName("Test")
                .lastName("User")
                .username("testuser")
                .build();

        validLoginRequest = LoginRequest.builder()
                .email("test@example.com")
                .username("testuser")
                .password("Test123!@#")
                .build();
    }

    @Test
    void registerUser_WithValidData_ShouldSucceed() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void registerUser_WithExistingEmail_ShouldFail() throws Exception {
        authService.register(validRegisterRequest);

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void login_WithValidCredentials_ShouldSucceed() throws Exception {
        authService.register(validRegisterRequest);

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void login_WithInvalidCredentials_ShouldFail() throws Exception {
        LoginRequest invalidRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("wrongpassword")
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_WithSQLInjection_ShouldFail() throws Exception {
        LoginRequest sqlInjectionRequest = LoginRequest.builder()
                .email("' OR '1'='1")
                .password("' OR '1'='1")
                .build();

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sqlInjectionRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void logout_ShouldSucceed() throws Exception {
        mockMvc.perform(post("/api/auth/logout")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}
