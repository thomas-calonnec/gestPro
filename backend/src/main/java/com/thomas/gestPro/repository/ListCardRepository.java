package com.thomas.gestPro.repository;

import com.thomas.gestPro.dto.ListCardDTO;
import com.thomas.gestPro.model.ListCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ListCardRepository extends JpaRepository<ListCard, Long> {

    List<ListCard> findByBoard_Id(Long id);
}
