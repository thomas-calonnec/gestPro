package com.thomas.gestPro.repository;

import com.thomas.gestPro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getUserByUsername(String username);
    Optional<User> findByEmail(String email);

}
