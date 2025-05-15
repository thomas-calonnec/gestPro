package com.thomas.gestPro.mapper;

import com.thomas.gestPro.dto.CheckListDTO;
import com.thomas.gestPro.model.CheckList;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CheckListMapper {

    CheckListDTO toDTO(CheckList checkList);

    @InheritInverseConfiguration
    CheckList toEntity(CheckListDTO checkListDTO);

}
