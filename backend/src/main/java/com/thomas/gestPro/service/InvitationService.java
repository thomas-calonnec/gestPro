package com.thomas.gestPro.service;

import com.thomas.gestPro.dto.UserDTO;
import com.thomas.gestPro.mapper.InvitationMapper;
import com.thomas.gestPro.mapper.UserMapper;
import com.thomas.gestPro.model.Invitation;
import com.thomas.gestPro.model.InvitationStatus;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.model.Workspace;
import com.thomas.gestPro.repository.InvitationRepository;
import com.thomas.gestPro.repository.UserRepository;
import com.thomas.gestPro.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class InvitationService {
    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserMapper userMapper;

    public InvitationService(InvitationRepository invitationRepository, UserRepository userRepository, WorkspaceRepository workspaceRepository, InvitationMapper invitationMapper, UserMapper userMapper) {
        this.invitationRepository = invitationRepository;
        this.userRepository = userRepository;
        this.workspaceRepository = workspaceRepository;
        this.userMapper = userMapper;
    }

    public void inviteUserToWorkspace(String email, Long workspaceId, User inviter) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("Workspace not found"));

        User inviteeOpt = userRepository.findByEmail(email).orElse(null);

        if (invitationRepository.existsByEmailAndWorkspace(email, workspace)) {
            throw new RuntimeException("User already invited.");
        }

        Invitation invitation = new Invitation();
        invitation.setEmail(email);
        invitation.setWorkspace(workspace);
        invitation.setInvitedBy(inviter);
        invitation.setStatus(InvitationStatus.PENDING);
        invitation.setInvitee(inviteeOpt);

        invitationRepository.save(invitation);

    }
    public void acceptInvitation(Long invitationId, User user) throws AccessDeniedException {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));

        if (!invitation.getEmail().equals(user.getEmail())) {
            throw new AccessDeniedException("You cannot accept this invitation");
        }

        Workspace workspace = invitation.getWorkspace();
        workspace.getUsers().add(user);
        workspaceRepository.save(workspace);

        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationRepository.save(invitation);
    }

    public List<Invitation> getUserInvitations(User user) {
        return invitationRepository.findByEmailAndStatus(user.getEmail(), InvitationStatus.PENDING);
    }

    public void rejectInvitation(Long invitationId, User user) throws AccessDeniedException {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));

        if (!invitation.getEmail().equals(user.getEmail())) {
            throw new AccessDeniedException("You cannot accept this invitation");
        }

        invitation.setStatus(InvitationStatus.DECLINED);
        invitationRepository.save(invitation);
    }

    public UserDTO getUserByInvitationId(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId).orElse(null);
        if (invitation == null) {
            throw new RuntimeException("Invitation not found");
        }
        User user = userRepository.findByEmail(invitation.getEmail()).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return userMapper.toDTO(user);
    }
}
