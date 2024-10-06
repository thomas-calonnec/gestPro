package com.thomas.gestPro.repository;

import com.thomas.gestPro.model.ListCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface ListCardRepository extends JpaRepository<ListCard, Long> {

}
