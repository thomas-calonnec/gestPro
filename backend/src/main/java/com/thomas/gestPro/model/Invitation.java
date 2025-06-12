package com.thomas.gestPro.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_invitations")
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email; // Celui de l'invité

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private InvitationStatus status = InvitationStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @ManyToOne
    @JoinColumn(name = "invited_by")
    private User invitedBy;

    @ManyToOne
    @JoinColumn(name = "invitee")
    private User invitee; // peut être null si l'utilisateur n'a pas encore créé de compte
}
