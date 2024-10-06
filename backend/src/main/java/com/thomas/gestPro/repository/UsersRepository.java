package com.thomas.gestPro.repository;

import com.thomas.gestPro.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByUserName(String userName);
}
