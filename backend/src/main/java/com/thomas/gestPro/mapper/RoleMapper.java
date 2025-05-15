package com.thomas.gestPro.mapper;

import com.thomas.gestPro.dto.RoleDTO;
import com.thomas.gestPro.model.Role;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
  RoleDTO toDTO(Role role);

  @InheritInverseConfiguration
  Role toEntity(RoleDTO roleDTO);
}
