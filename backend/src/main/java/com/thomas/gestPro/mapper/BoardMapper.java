package com.thomas.gestPro.mapper;

import com.thomas.gestPro.dto.BoardDTO;
import com.thomas.gestPro.dto.BoardLightDTO;
import com.thomas.gestPro.model.Board;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BoardMapper {

    BoardDTO toDTO(Board board);

    BoardLightDTO toLightDTO(Board board);

    @InheritInverseConfiguration
    Board toEntity(BoardDTO boardDTO);

}
