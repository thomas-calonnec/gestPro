package com.thomas.gestPro.service;

import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.dto.CardDTO;
import com.thomas.gestPro.dto.LabelDTO;
import com.thomas.gestPro.mapper.CardMapper;
import com.thomas.gestPro.model.Card;
import com.thomas.gestPro.model.Label;
import com.thomas.gestPro.repository.CardRepository;
import com.thomas.gestPro.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LabelService {

    private final LabelRepository labelRepository;
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    /**
     * Constructor with dependency injection for LabelRepository.
     *
     * @param labelRepository repository for managing labels
     */
    @Autowired
    public LabelService(LabelRepository labelRepository, CardRepository cardRepository, CardMapper cardMapper) {
        this.labelRepository = labelRepository;

        this.cardRepository = cardRepository;
        this.cardMapper = cardMapper;
    }

    /**
     * Finds a Label by its ID.
     *
     * @param id the ID of the Label to find
     * @return the found Label
     * @throws ResourceNotFoundException if no Label is found with the given ID
     */
    public Label findLabelById(Long id) {
        return labelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Label not found"));
    }

    /**
     * Retrieves all the labels from the repository.
     *
     * @return a list of all labels
     */
    public List<Label> findAllLabel() {
        return labelRepository.findAll();
    }

    /**
     * Creates a new Label if it doesn't already exist.
     *
     * @param label the Label to create
     * @throws RuntimeException if a Label with the same color already exists
     */
    public void createLabel(Label label) {
        if(labelRepository.findLabelByColor(label.getColor()).isEmpty()) {
            labelRepository.save(label);
        } else {
            throw new RuntimeException("Label already exists");
        }

    }

    /**
     * Updates an existing Label with new details.
     *
     * @param id the ID of the Label to update
     * @param updateLabel the Label object with updated values
     * @return the updated Label
     */
    public Label updateLabel(Long id, Label updateLabel){
        Label existingLabel = findLabelById(id);
        existingLabel.setColor(updateLabel.getColor());
        return labelRepository.save(existingLabel);
    }

    /**
     * Deletes a Label by its ID.
     *
     * @param id the ID of the Label to delete
     */
    public void deleteLabel(Long id) {
        labelRepository.deleteById(id);
    }

    /**
     * Adds a label with a specific color to a card.
     *
     * @param cardId the ID of the card
     * @param updateLabel the label to add to the card
     * @return the updated card with the label added
     * @throws ResourceNotFoundException if the label is not found
     */
    public CardDTO addCardLabelColor(Long cardId, LabelDTO updateLabel){
        Label label = labelRepository.findLabelByColor(updateLabel.getColor())
                .orElseThrow(() -> new ResourceNotFoundException("Label not Found"));

        Card existingCard = getCardById(cardId);

        // Ajouter le label à la carte et la carte au label
        existingCard.getLabels().add(label);
        label.getCards().add(existingCard);
        labelRepository.save(label);

        // Sauvegarder uniquement la carte. Hibernate gérera la relation Many-to-Many.
        cardRepository.save(existingCard);

        return cardMapper.toDTO(existingCard);
    }

    /**
     * Removes a label with a specific color from a card.
     *
     * @param cardId the ID of the card
     * @param labelColor the color of the label to remove
     */
    public void removeLabelFromCard(Long cardId, String labelColor) {
        // Retrieve the card by ID
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        // Retrieve the label by color
        Label label = labelRepository.findLabelByColor(labelColor)
                .orElseThrow(() -> new RuntimeException("Label not found"));

        // Remove the label from the card's collection
        card.getLabels().remove(label);

        // Remove the card from the label's collection (bidirectional relationship)
        label.getCards().remove(card);

        // Save both entities to update the relationship
        cardRepository.save(card);
        labelRepository.save(label);
    }

    /**
     * Finds a card by its ID.
     *
     * @param cardId the ID of the card to retrieve
     * @return the card with the given ID
     * @throws ResourceNotFoundException if the card is not found
     */
    public Card getCardById(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(() -> new ResourceNotFoundException("Card not found"));
    }
}
