package com.thomas.gestPro.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    private Long id;
    private String name;
    private String color;
    private String description;
    private Date lastUpdated;
    private Integer cardCount;
    private String status;
    private Long ownerId;
    private List<ListCardDTO> listCards;
    private List<UserDTO> members;
   

}