package com.thomas.gestPro.service.integration;

import com.thomas.gestPro.model.Label;
import com.thomas.gestPro.repository.LabelRepository;
import com.thomas.gestPro.service.LabelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class LabelServiceIntegrationTest {

    @Autowired
    private LabelService labelService;

    @Autowired
    private LabelRepository labelRepository;

    @Test
    void createLabel_ShouldSaveAndReturnLabel() {
        Label label = Label.builder()
                .color("#FF0000")
                .build();

        Label savedLabel = labelService.createLabel(label);

        assertNotNull(savedLabel.getId());
        assertEquals("#FF0000", savedLabel.getColor());
    }

    @Test
    void getLabelById_ShouldReturnLabel() {
        Label label = Label.builder()
                .color("#FF0000")
                .build();
        Label savedLabel = labelRepository.save(label);

        Label foundLabel = labelService.findLabelById(savedLabel.getId());

        assertNotNull(foundLabel);
        assertEquals(savedLabel.getId(), foundLabel.getId());
    }

    @Test
    void updateLabel_ShouldUpdateExistingLabel() {
        Label label = Label.builder()
                .color("#FF0000")
                .build();
        Label savedLabel = labelRepository.save(label);

        Label updatedLabel = labelService.updateLabel(savedLabel.getId(), savedLabel);

        assertEquals(savedLabel.getId(), updatedLabel.getId());
    }

    @Test
    void deleteLabel_ShouldRemoveLabel() {
        Label label = Label.builder()
                .color("#FF0000")
                .build();
        Label savedLabel = labelRepository.save(label);

        labelService.deleteLabel(savedLabel.getId());

        assertFalse(labelRepository.existsById(savedLabel.getId()));
    }
}
