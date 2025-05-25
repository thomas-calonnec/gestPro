package com.thomas.gestPro.mapper;

import com.thomas.gestPro.dto.BoardLightDTO;
import com.thomas.gestPro.model.Board;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardLightMapper {

    BoardLightDTO toDTO(Board board);

    @InheritInverseConfiguration
    Board toEntity(BoardLightDTO boardLightDTO);
}
