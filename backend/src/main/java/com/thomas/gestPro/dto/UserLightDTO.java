package com.thomas.gestPro.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLightDTO {
    private Long id;
    private String username;
    private String email;
    private String pictureUrl;
}
