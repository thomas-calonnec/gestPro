package com.thomas.gestPro.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
