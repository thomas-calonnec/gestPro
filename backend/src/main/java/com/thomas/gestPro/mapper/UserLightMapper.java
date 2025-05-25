package com.thomas.gestPro.mapper;

import com.thomas.gestPro.dto.UserLightDTO;
import com.thomas.gestPro.model.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserLightMapper {
    UserLightDTO toDTO(User user);

    @InheritInverseConfiguration
    User toEntity(UserLightDTO userLightDTO);
}
