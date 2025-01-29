package com.thomas.gestPro.repository;

import com.thomas.gestPro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByUsername(String username);
}
