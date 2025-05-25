package com.thomas.gestPro.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLightDTO {
    private Long id;
    private String username;
    private String email;
    private String pictureUrl;
}
