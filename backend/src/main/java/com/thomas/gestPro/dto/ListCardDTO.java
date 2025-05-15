package com.thomas.gestPro.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ListCardDTO {

    private Long id;
    private String name;
    private int orderIndex;
    private Boolean isArchived;
    private List<CardDTO> cards;
}
