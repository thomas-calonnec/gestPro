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
public class WorkspaceDTO {
    private Long id;
    private String name;
    private String description;
    private Date updateAt;
    private Boolean isFavorite;
    private List<BoardDTO> boards;

}
