package com.thomas.gestPro.repository;

import com.thomas.gestPro.model.CheckList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckListRepository extends JpaRepository<CheckList, Long> {
}
