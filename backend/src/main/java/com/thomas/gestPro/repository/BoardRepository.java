package com.thomas.gestPro.repository;

import com.thomas.gestPro.model.Board;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
Optional<Board> getBoardByBoardName(String boardName);
}
