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

    @Autowired
    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;

    }

    public Label findLabelById(Long id) {
        return labelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Label not found"));
    }

    public List<Label> findAllLabel() {
        return labelRepository.findAll();
    }

    public void createLabel(Label label) {
        if(labelRepository.findLabelByLabelColor(label.getLabelColor()).isEmpty()) {
            labelRepository.save(label);
        } else {
            throw new RuntimeException("Label already exists");
        }

    }

    public Label updateLabel(Long id, Label updateLabel){
        Label existingLabel = findLabelById(id);
        existingLabel.setLabelColor(updateLabel.getLabelColor());
        return labelRepository.save(existingLabel);
    }

    public void deleteLabel(Long id) {
        labelRepository.deleteById(id);
    }
}
