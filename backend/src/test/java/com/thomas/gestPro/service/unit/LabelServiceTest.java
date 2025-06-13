package com.thomas.gestPro.service.unit;

import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.dto.CardDTO;
import com.thomas.gestPro.dto.LabelDTO;
import com.thomas.gestPro.mapper.CardMapper;
import com.thomas.gestPro.model.Card;
import com.thomas.gestPro.model.Label;
import com.thomas.gestPro.repository.CardRepository;
import com.thomas.gestPro.repository.LabelRepository;
import com.thomas.gestPro.service.LabelService;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class LabelServiceTest {

    @Mock
    private LabelRepository mockLabelRepository;
    @Mock
    private CardRepository mockCardRepository;
    @Mock
    private CardMapper mockCardMapper;

    private LabelService labelServiceUnderTest;

    @BeforeEach
    void setUp() {
        labelServiceUnderTest = new LabelService(mockLabelRepository, mockCardRepository, mockCardMapper);
    }

    @Test
    void testFindLabelById() {
        // Setup
        final Label expectedResult = Label.builder()
                .color("color")
                .cards(List.of(Card.builder()
                        .labels(List.of())
                        .build()))
                .build();

        // Configure LabelRepository.findById(...).
        final Optional<Label> label = Optional.of(Label.builder()
                .color("color")
                .cards(List.of(Card.builder()
                        .labels(List.of())
                        .build()))
                .build());
        when(mockLabelRepository.findById(0L)).thenReturn(label);

        // Run the test
        final Label result = labelServiceUnderTest.findLabelById(0L);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testFindLabelById_LabelRepositoryReturnsAbsent() {
        // Setup
        when(mockLabelRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> labelServiceUnderTest.findLabelById(0L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testFindAllLabel() {
        // Setup
        final List<Label> expectedResult = List.of(Label.builder()
                .color("color")
                .cards(List.of(Card.builder().build()))
                .build());

        // Configure LabelRepository.findAll(...).
        final List<Label> labels = List.of(Label.builder()
                .color("color")
                .cards(List.of(Card.builder().build()))
                .build());
        when(mockLabelRepository.findAll()).thenReturn(labels);

        // Run the test
        final List<Label> result = labelServiceUnderTest.findAllLabel();

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testFindAllLabel_LabelRepositoryReturnsNoItems() {
        // Setup
        when(mockLabelRepository.findAll()).thenReturn(Collections.emptyList());

        // Run the test
        final List<Label> result = labelServiceUnderTest.findAllLabel();

        // Verify the results
        assertThat(result).isEqualTo(Collections.emptyList());
    }

    @Test
    void testCreateLabel_ThrowsRuntimeException() {
        // Setup
        final Label label = Label.builder()
                .color("color")
                .cards(List.of(Card.builder()
                        .labels(List.of())
                        .build()))
                .build();

        // Configure LabelRepository.findLabelByColor(...).
        final Optional<Label> label1 = Optional.of(Label.builder()
                .color("color")
                .cards(List.of(Card.builder()
                        .labels(List.of())
                        .build()))
                .build());
        when(mockLabelRepository.findLabelByColor("color")).thenReturn(label1);

        // Run the test
        assertThatThrownBy(() -> labelServiceUnderTest.createLabel(label)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void testCreateLabel_LabelRepositoryFindLabelByColorReturnsAbsent() {
        // Setup
        final Label label = Label.builder()
                .color("color")
                .cards(List.of(Card.builder()
                        .labels(List.of())
                        .build()))
                .build();
        when(mockLabelRepository.findLabelByColor("color")).thenReturn(Optional.empty());

        // Run the test
        labelServiceUnderTest.createLabel(label);

        // Verify the results
        verify(mockLabelRepository).save(Label.builder()
                .color("color")
                .cards(List.of(Card.builder()
                        .labels(List.of())
                        .build()))
                .build());
    }

    @Test
    void testCreateLabel_LabelRepositorySaveThrowsOptimisticLockingFailureException() {
        // Setup
        final Label label = Label.builder()
                .color("color")
                .cards(List.of(Card.builder()
                        .labels(List.of())
                        .build()))
                .build();
        when(mockLabelRepository.findLabelByColor("color")).thenReturn(Optional.empty());
        when(mockLabelRepository.save(Label.builder()
                .color("color")
                .cards(List.of(Card.builder()
                        .labels(List.of())
                        .build()))
                .build())).thenThrow(OptimisticLockingFailureException.class);

        // Run the test
        assertThatThrownBy(() -> labelServiceUnderTest.createLabel(label))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    @Test
    void testUpdateLabel() {
        // Setup
        final Label updateLabel = Label.builder()
                .color("color")
                .cards(List.of(Card.builder()
                        .labels(List.of())
                        .build()))
                .build();
        final Label expectedResult = Label.builder()
                .color("color")
                .cards(List.of(Card.builder()
                        .labels(List.of())
                        .build()))
                .build();

        // Configure LabelRepository.findById(...).
        final Optional<Label> label = Optional.of(Label.builder()
                .color("color")
                .cards(List.of(Card.builder()
                        .labels(List.of())
                        .build()))
                .build());
        when(mockLabelRepository.findById(0L)).thenReturn(label);

        // Configure LabelRepository.save(...).
        final Label label1 = Label.builder()
                .color("color")
                .cards(List.of(Card.builder()
                        .labels(List.of())
                        .build()))
                .build();
        when(mockLabelRepository.save(Label.builder()
                .color("color")
                .cards(List.of(Card.builder()
                        .labels(List.of())
                        .build()))
                .build())).thenReturn(label1);

        // Run the test
        final Label result = labelServiceUnderTest.updateLabel(0L, updateLabel);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testUpdateLabel_LabelRepositoryFindByIdReturnsAbsent() {
        // Setup
        final Label updateLabel = Label.builder()
                .color("color")
                .cards(List.of(Card.builder()
                        .labels(List.of())
                        .build()))
                .build();
        when(mockLabelRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> labelServiceUnderTest.updateLabel(0L, updateLabel))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testUpdateLabel_LabelRepositorySaveThrowsOptimisticLockingFailureException() {
        // Setup
        final Label updateLabel = Label.builder()
                .color("color")
                .cards(List.of(Card.builder()
                        .labels(List.of())
                        .build()))
                .build();

        // Configure LabelRepository.findById(...).
        final Optional<Label> label = Optional.of(Label.builder()
                .color("color")
                .cards(List.of(Card.builder()
                        .labels(List.of())
                        .build()))
                .build());
        when(mockLabelRepository.findById(0L)).thenReturn(label);

        when(mockLabelRepository.save(Label.builder()
                .color("color")
                .cards(List.of(Card.builder()
                        .labels(List.of())
                        .build()))
                .build())).thenThrow(OptimisticLockingFailureException.class);

        // Run the test
        assertThatThrownBy(() -> labelServiceUnderTest.updateLabel(0L, updateLabel))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    @Test
    void testDeleteLabel() {
        // Setup
        // Run the test
        labelServiceUnderTest.deleteLabel(0L);

        // Verify the results
        verify(mockLabelRepository).deleteById(0L);
    }

    @Test
    void testAddCardLabelColor() {
        // Préparation des données d'entrée
        final LabelDTO labelDtoToUpdate = new LabelDTO(0L, "color");

        // Création du label simulé
        final Label labelWithColor = Label.builder()
                .color("color")
                .cards(new ArrayList<>(
                        List.of(Card.builder()
                                .labels(new ArrayList<>())
                                .build())))
                .build();
        when(mockLabelRepository.findLabelByColor("color")).thenReturn(Optional.of(labelWithColor));

        // Création de la carte simulée
        final Card cardWithLabel = Card.builder()
                .labels(new ArrayList<>(
                        List.of(Label.builder()
                                .color("color")
                                .cards(new ArrayList<>())
                                .build())))
                .build();
        when(mockCardRepository.findById(0L)).thenReturn(Optional.of(cardWithLabel));

        // Création du DTO attendu
        final CardDTO expectedCardDTO = new CardDTO();
        expectedCardDTO.setId(0L);
        expectedCardDTO.setName("name");
        expectedCardDTO.setDescription("description");
        expectedCardDTO.setDeadline(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
        expectedCardDTO.setHours(0);

        when(mockCardMapper.toDTO(cardWithLabel)).thenReturn(expectedCardDTO);

        // Exécution de la méthode testée
        final CardDTO result = labelServiceUnderTest.addCardLabelColor(0L, labelDtoToUpdate);

        // Vérification des sauvegardes (mock interactions)
        verify(mockLabelRepository).save(labelWithColor);
    }

    @Test
    void testAddCardLabelColor_LabelRepositoryFindLabelByColorReturnsAbsent() {
        // Setup
        final LabelDTO updateLabel = new LabelDTO(0L, "color");
        when(mockLabelRepository.findLabelByColor("color")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> labelServiceUnderTest.addCardLabelColor(0L, updateLabel))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testAddCardLabelColor_ThrowsResourceNotFoundException_WhenCardNotFound() {
        // Setup
        final LabelDTO updateLabel = new LabelDTO(0L, "color");
        final Optional<Label> label = Optional.of(Label.builder().color("color").cards(new ArrayList<>()).build());
        when(mockLabelRepository.findLabelByColor("color")).thenReturn(label);
        when(mockCardRepository.findById(0L)).thenReturn(Optional.empty());

        // Run + Verify
        assertThatThrownBy(() -> labelServiceUnderTest.addCardLabelColor(0L, updateLabel))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Card not found");

        verify(mockCardRepository).findById(0L);
    }

    @Test
    void testAddCardLabelColor_LabelRepositorySaveThrowsOptimisticLockingFailureException() {
        final LabelDTO updateLabel = new LabelDTO(0L, "color");

        // Création d'instances partagées avec des listes mutables
        final Label expectedLabel = Label.builder()
                .color("color")
                .cards(new ArrayList<>())
                .build();
        final Card expectedCard = Card.builder()
                .labels(new ArrayList<>(List.of(expectedLabel)))
                .build();
        expectedLabel.getCards().add(expectedCard);

        when(mockLabelRepository.findLabelByColor("color")).thenReturn(Optional.of(expectedLabel));
        when(mockCardRepository.findById(0L)).thenReturn(Optional.of(expectedCard));
        when(mockLabelRepository.save(expectedLabel)).thenThrow(OptimisticLockingFailureException.class);

        assertThatThrownBy(() -> labelServiceUnderTest.addCardLabelColor(0L, updateLabel))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    @Test
    void testAddCardLabelColor_CardRepositorySaveThrowsOptimisticLockingFailureException() {
        final LabelDTO updateLabel = new LabelDTO(0L, "color");

        // Création des objets de test avec collections mutables partagées
        final Label expectedLabel = Label.builder()
                .color("color")
                .cards(new ArrayList<>())
                .build();
        final Card expectedCard = Card.builder()
                .labels(new ArrayList<>(List.of(expectedLabel)))
                .build();
        expectedLabel.getCards().add(expectedCard);

        when(mockLabelRepository.findLabelByColor("color")).thenReturn(Optional.of(expectedLabel));
        when(mockCardRepository.findById(0L)).thenReturn(Optional.of(expectedCard));
        when(mockCardRepository.save(expectedCard)).thenThrow(OptimisticLockingFailureException.class);

        assertThatThrownBy(() -> labelServiceUnderTest.addCardLabelColor(0L, updateLabel))
                .isInstanceOf(OptimisticLockingFailureException.class);
        verify(mockLabelRepository).save(expectedLabel);
    }

    @Test
    void testRemoveLabelFromCard() {
        // Setup
        // Configure CardRepository.findById(...).
        final Optional<Card> card = Optional.of(Card.builder()
                .labels(new ArrayList<>(List.of(Label.builder()
                        .color("color")
                        .cards(new ArrayList<>(List.of()))
                        .build())))
                .build());
        when(mockCardRepository.findById(0L)).thenReturn(card);

        // Configure LabelRepository.findLabelByColor(...).
        final Optional<Label> label = Optional.of(Label.builder()
                .color("color")
                .cards(new ArrayList<>(List.of(Card.builder()
                        .labels(new ArrayList<>(List.of()))
                        .build())))
                .build());
        when(mockLabelRepository.findLabelByColor("labelColor")).thenReturn(label);

        // Run the test
        labelServiceUnderTest.removeLabelFromCard(0L, "labelColor");

        // Verify the results
        verify(mockCardRepository).save(Card.builder()
                .labels(List.of(Label.builder()
                        .color("color")
                        .cards(List.of())
                        .build()))
                .build());
        verify(mockLabelRepository).save(Label.builder()
                .color("color")
                .cards(List.of(Card.builder()
                        .labels(List.of())
                        .build()))
                .build());
    }

    @Test
    void testRemoveLabelFromCard_CardRepositoryFindByIdReturnsAbsent() {
        // Setup
        when(mockCardRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> labelServiceUnderTest.removeLabelFromCard(0L, "labelColor"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void testRemoveLabelFromCard_LabelRepositoryFindLabelByColorReturnsAbsent() {
        // Setup
        // Configure CardRepository.findById(...).
        final Optional<Card> card = Optional.of(Card.builder()
                .labels(List.of(Label.builder()
                        .color("color")
                        .cards(List.of())
                        .build()))
                .build());
        when(mockCardRepository.findById(0L)).thenReturn(card);

        when(mockLabelRepository.findLabelByColor("labelColor")).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> labelServiceUnderTest.removeLabelFromCard(0L, "labelColor"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void testRemoveLabelFromCard_CardRepositorySaveThrowsOptimisticLockingFailureException() {
        // Setup
        // Configure CardRepository.findById(...).
        final Optional<Card> card = Optional.of(Card.builder()
                .labels(new ArrayList<>(List.of(Label.builder()
                        .color("color")
                        .cards(new ArrayList<>(List.of()))
                        .build())))
                .build());
        when(mockCardRepository.findById(0L)).thenReturn(card);

        // Configure LabelRepository.findLabelByColor(...).
        final Optional<Label> label = Optional.of(Label.builder()
                .color("color")
                .cards(new ArrayList<>(List.of(Card.builder()
                        .labels(new ArrayList<>(List.of()))
                        .build())))
                .build());
        when(mockLabelRepository.findLabelByColor("labelColor")).thenReturn(label);

        when(mockCardRepository.save(Card.builder()
                .labels(new ArrayList<>(List.of(Label.builder()
                        .color("color")
                        .cards(new ArrayList<>(List.of()))
                        .build())))
                .build())).thenThrow(OptimisticLockingFailureException.class);

        // Run the test
        assertThatThrownBy(() -> labelServiceUnderTest.removeLabelFromCard(0L, "labelColor"))
                .isInstanceOf(OptimisticLockingFailureException.class);
    }

    // Test refactorisé pour la lisibilité et la réutilisation
    @Test
    void testRemoveLabelFromCard_LabelRepositorySaveThrowsOptimisticLockingFailureException() {
        final String labelColor = "color";
        final Long cardId = 0L;

        // Utiliser ArrayList pour pouvoir modifier les listes (ajout/suppression)
        final Label label = Label.builder()
                .color(labelColor)
                .cards(new ArrayList<>())
                .build();
        final Card card = Card.builder()
                .labels(new ArrayList<>(List.of(label)))
                .build();
        label.getCards().add(card);

        when(mockCardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(mockLabelRepository.findLabelByColor("labelColor")).thenReturn(Optional.of(label));
        when(mockLabelRepository.save(label)).thenThrow(OptimisticLockingFailureException.class);

        assertThatThrownBy(() -> labelServiceUnderTest.removeLabelFromCard(cardId, "labelColor"))
                .isInstanceOf(OptimisticLockingFailureException.class);
        verify(mockCardRepository).save(card);
    }
    @Test
    void testGetCardById() {
        // Setup
        final Card expectedResult = Card.builder()
                .labels(List.of(Label.builder()
                        .color("color")
                        .cards(List.of())
                        .build()))
                .build();

        // Configure CardRepository.findById(...).
        final Optional<Card> card = Optional.of(Card.builder()
                .labels(List.of(Label.builder()
                        .color("color")
                        .cards(List.of())
                        .build()))
                .build());
        when(mockCardRepository.findById(0L)).thenReturn(card);

        // Run the test
        final Card result = labelServiceUnderTest.getCardById(0L);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetCardById_CardRepositoryReturnsAbsent() {
        // Setup
        when(mockCardRepository.findById(0L)).thenReturn(Optional.empty());

        // Run the test
        assertThatThrownBy(() -> labelServiceUnderTest.getCardById(0L)).isInstanceOf(ResourceNotFoundException.class);
    }
}
