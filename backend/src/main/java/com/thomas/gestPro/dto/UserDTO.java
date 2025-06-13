package com.thomas.gestPro.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String pictureUrl;
    private List<WorkspaceDTO> workspaces;
    private List<RoleDTO> roles;

}
