package com.thomas.gestPro.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String pictureUrl;
    private List<WorkspaceDTO> workspaces;
    private List<RoleDTO> roles;

}
