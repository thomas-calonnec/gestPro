package com.thomas.gestPro.mapper;

import com.thomas.gestPro.dto.CardDTO;
import com.thomas.gestPro.model.Card;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {

    CardDTO toDTO(Card card);

    @InheritInverseConfiguration
    Card toCard(CardDTO cardDTO);

}
