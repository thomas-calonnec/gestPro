package com.thomas.gestPro.mapper;

import com.thomas.gestPro.dto.UserDTO;
import com.thomas.gestPro.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {WorkspaceMapper.class})
public interface UserMapper {

       UserDTO toDTO(User user);

       @InheritInverseConfiguration
       User toEntity(UserDTO userDTO);

}

