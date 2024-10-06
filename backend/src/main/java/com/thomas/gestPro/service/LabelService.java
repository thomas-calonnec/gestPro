package com.thomas.gestPro.service;

import com.thomas.gestPro.Exception.ResourceNotFoundException;
import com.thomas.gestPro.model.Label;
import com.thomas.gestPro.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LabelService {

    private final LabelRepository labelRepository;

    /**
     * Constructor with dependency injection for LabelRepository.
     *
     * @param labelRepository repository for managing labels
     */
    @Autowired
    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;

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
        if(labelRepository.findLabelByLabelColor(label.getLabelColor()).isEmpty()) {
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
        existingLabel.setLabelColor(updateLabel.getLabelColor());
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
}
