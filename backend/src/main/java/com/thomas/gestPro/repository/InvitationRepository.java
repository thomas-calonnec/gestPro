package com.thomas.gestPro.repository;

import com.thomas.gestPro.model.Invitation;
import com.thomas.gestPro.model.InvitationStatus;
import com.thomas.gestPro.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    boolean existsByEmailAndWorkspace(String email, Workspace workspace);

    List<Invitation> findByEmailAndStatus(String email, InvitationStatus invitationStatus);
}
