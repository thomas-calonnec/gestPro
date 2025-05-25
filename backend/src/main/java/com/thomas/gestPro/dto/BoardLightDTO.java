package com.thomas.gestPro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardLightDTO {
    private Long id;
    private String name;
    private String color;
    private String description;
    private Date lastUpdated;
    private Integer cardCount;
    private String status;
    private Long ownerId;
}
