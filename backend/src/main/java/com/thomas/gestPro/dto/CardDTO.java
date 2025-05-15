package com.thomas.gestPro.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime deadline;
    private Integer hours;
    private Integer minutes;
    private String  status;
    private Boolean isCompleted;
    private Boolean isDateActivated;
    private Boolean isLabelActivated;
    private Boolean isChecklistActivated;
    private List<CheckListDTO> checkList;


}
