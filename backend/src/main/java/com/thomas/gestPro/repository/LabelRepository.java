package com.thomas.gestPro.repository;

import com.thomas.gestPro.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label, Long> {

    Optional<Label> findLabelByLabelColor(String labelColor);
}
