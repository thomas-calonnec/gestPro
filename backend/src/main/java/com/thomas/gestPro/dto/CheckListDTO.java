package com.thomas.gestPro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckListDTO {

    private Long id;
    private String name;
    private Boolean completed;
}
