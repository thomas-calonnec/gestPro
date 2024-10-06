package com.thomas.gestPro.controller;

import com.thomas.gestPro.model.Label;
import com.thomas.gestPro.service.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/labels")
public class LabelController {

    private final LabelService labelService;

    @Autowired
    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping
    public ResponseEntity<List<Label>> getAllLabel(){
        return ResponseEntity.ok(labelService.findAllLabel());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Label> getLabelById(@PathVariable Long id) {
        return ResponseEntity.ok(labelService.findLabelById(id));
    }


    @PutMapping("/create")
    public ResponseEntity<String> createLabel(@RequestBody Label label) {
        labelService.createLabel(label);
        return ResponseEntity.ok("Label created");
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Label> updateLabelById(@PathVariable Long id, @RequestBody Label Label) {
        Label updateLabel = labelService.updateLabel(id,Label);
        return ResponseEntity.ok(updateLabel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLabelById(@PathVariable Long id) {
        labelService.deleteLabel(id);
        return ResponseEntity.noContent().build();
    }


}
