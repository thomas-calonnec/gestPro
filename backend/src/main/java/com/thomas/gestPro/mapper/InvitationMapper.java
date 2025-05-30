package com.thomas.gestPro.mapper;

import com.thomas.gestPro.dto.InvitationDTO;
import com.thomas.gestPro.model.Invitation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvitationMapper {
    InvitationDTO toDTO(Invitation invitation);

    Invitation toEntity(InvitationDTO invitationDTO);
}
