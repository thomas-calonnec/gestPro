package com.thomas.gestPro.Security;

import com.thomas.gestPro.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtResponse {

    private  UserDTO user;
    private  String message;

    public JwtResponse(UserDTO user) {
        this.user = user;
    }
    public JwtResponse(String message) {
        this.message = message;
    }
}