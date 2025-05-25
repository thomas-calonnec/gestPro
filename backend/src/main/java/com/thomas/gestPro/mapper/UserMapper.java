package com.thomas.gestPro.mapper;

import com.thomas.gestPro.dto.UserDTO;
import com.thomas.gestPro.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

       @Mapping(target = "workspaces", ignore = true)
       UserDTO toDTO(User user);

       @InheritInverseConfiguration
       User toEntity(UserDTO userDTO);

}

