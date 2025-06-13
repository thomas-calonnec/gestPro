package com.thomas.gestPro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thomas.gestPro.dto.CardDTO;
import com.thomas.gestPro.mapper.CardMapper;
import com.thomas.gestPro.model.Card;
import com.thomas.gestPro.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CardService cardService;

    @Autowired
    private ObjectMapper objectMapper;

    private CardDTO cardDTO;
    private List<CardDTO> cardDTOs;
    @Autowired
    private CardMapper cardMapper;

    @BeforeEach
    void setUp() {
        cardDTO = new CardDTO();
        cardDTO.setId(1L);
        cardDTO.setName("Test Card");
        cardDTO.setDescription("Test Description");

        cardDTOs = List.of(cardDTO);
    }

    @Test
    void getCardById() throws Exception {
        when(cardService.getCardById(1L)).thenReturn(cardMapper.toEntity(cardDTO));

        mockMvc.perform(get("/api/user/cards/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Card"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    void createCard() throws Exception {
        when(cardService.createCard(eq(1L), any(Card.class))).thenReturn(cardDTO);

        mockMvc.perform(post("/api/user/cards/1/card")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Card"));
    }

    @Test
    void updateCardById() throws Exception {
        when(cardService.updateCard(eq(1L), any(CardDTO.class))).thenReturn(cardDTO);

        mockMvc.perform(put("/api/user/cards/1/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Card"));
    }

    @Test
    void deleteCardById() throws Exception {
        mockMvc.perform(delete("/api/user/cards/1"))
                .andExpect(status().isNoContent());
    }
}