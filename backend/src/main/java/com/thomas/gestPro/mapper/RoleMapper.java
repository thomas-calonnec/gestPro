package com.thomas.gestPro.mapper;

import com.thomas.gestPro.dto.RoleDTO;
import com.thomas.gestPro.model.Role;

public class RoleMapper {
    public static RoleDTO toRoleDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(role.getId());
        roleDTO.setName(role.getName());
        return roleDTO;
    }
}
