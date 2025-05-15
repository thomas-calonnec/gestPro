package com.thomas.gestPro.mapper;

import com.thomas.gestPro.dto.LabelDTO;
import com.thomas.gestPro.model.Label;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;


@Mapper(componentModel = "string")
public interface LabelMapper {
  LabelDTO toDto(Label label);

  @InheritInverseConfiguration
  Label toEntity(LabelDTO labelDTO);
}
