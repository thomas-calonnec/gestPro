package com.thomas.gestPro.dto;

import java.util.List;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListCardDTO {

    private Long id;
    private String name;
    private int orderIndex;
    private Boolean isArchived;
    private BoardLightDTO board;
    private List<CardDTO> cards;
}
