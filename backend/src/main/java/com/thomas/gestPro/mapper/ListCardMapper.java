package com.thomas.gestPro.mapper;

import com.thomas.gestPro.dto.CardDTO;
import com.thomas.gestPro.dto.ListCardDTO;
import com.thomas.gestPro.model.ListCard;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses = {CardMapper.class})
public interface ListCardMapper {

      ListCardDTO toDTO(ListCard listCard);

      @InheritInverseConfiguration
      ListCard toEntity(CardDTO cardDTO);

}
