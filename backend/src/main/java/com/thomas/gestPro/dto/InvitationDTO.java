package com.thomas.gestPro.dto;

import com.thomas.gestPro.model.InvitationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvitationDTO {
    private Long id;
    private String email;
    private InvitationStatus status = InvitationStatus.PENDING;
    private WorkspaceDTO workspace;
    private UserDTO invitedBy;
    private UserDTO invitee;
}
