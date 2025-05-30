package com.thomas.gestPro.controller;

import com.thomas.gestPro.dto.InvitationDTO;
import com.thomas.gestPro.dto.UserDTO;
import com.thomas.gestPro.mapper.InvitationMapper;
import com.thomas.gestPro.mapper.UserMapper;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.service.InvitationService;
import com.thomas.gestPro.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user/invitations")
@RequiredArgsConstructor
public class InvitationController {

    private final InvitationService invitationService;
    private final UserService userService; // pour récupérer l'utilisateur connecté
    private final UserMapper userMapper;
    private final InvitationMapper invitationMapper;

    // 1. Créer une invitation
    @PostMapping("/invite")
    public ResponseEntity<Void> inviteToWorkspace(
            @RequestBody InvitationDTO invitationInput,
                Principal principal
    ) {
        User inviter = userMapper.toEntity(userService.getUserByUsername(principal.getName()));
        invitationService.inviteUserToWorkspace(invitationInput.getEmail(), invitationInput.getWorkspace().getId(), inviter);
        return ResponseEntity.ok().build();
    }

    // 2. Obtenir les invitations du user connecté
    @GetMapping("/me/{id}")
    public ResponseEntity<List<InvitationDTO>> getMyInvitations(Principal principal, @PathVariable Long id) {
        User user = userMapper.toEntity(userService.getById(id));
        List<InvitationDTO> invitations = invitationService.getUserInvitations(user).stream()
                .map(invitationMapper::toDTO)
                .toList();
        return ResponseEntity.ok(invitations);
    }

    // 3. Accepter une invitation
    @PostMapping("/{invitationId}/accept")
    public ResponseEntity<Void> acceptInvitation(
            @PathVariable Long invitationId,
            Principal principal
    ) throws AccessDeniedException {

        UserDTO user;
        if(principal != null) {
            user = userService.getUserByUsername(principal.getName());

        } else {
            user = invitationService.getUserByInvitationId(invitationId);
        }

        invitationService.acceptInvitation(invitationId, userMapper.toEntity(user));
        return ResponseEntity.ok().build();
    }

    // 4. Refuser une invitation
    @PostMapping("/{invitationId}/reject")
    public ResponseEntity<Void> rejectInvitation(
            @PathVariable Long invitationId,
            Principal principal
    ) throws AccessDeniedException {
        UserDTO user;
        if(principal != null) {
            user = userService.getUserByUsername(principal.getName());

        } else {
            user = invitationService.getUserByInvitationId(invitationId);
        }
        invitationService.rejectInvitation(invitationId, userMapper.toEntity(user));
        return ResponseEntity.ok().build();
    }
}
