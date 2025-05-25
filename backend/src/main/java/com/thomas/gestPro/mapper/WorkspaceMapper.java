package com.thomas.gestPro.mapper;

import com.thomas.gestPro.dto.WorkspaceDTO;
import com.thomas.gestPro.model.Workspace;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkspaceMapper {

     WorkspaceDTO toDTO(Workspace workspace);

     @InheritInverseConfiguration
     Workspace toEntity(WorkspaceDTO workspaceDTO);

}
