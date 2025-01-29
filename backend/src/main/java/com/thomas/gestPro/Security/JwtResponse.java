package com.thomas.gestPro.Security;

import com.thomas.gestPro.model.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtResponse {

    private  User user;
    private  String message;

    public JwtResponse(String message, User user) {
        this.user = user;
        this.message = message;
    }
    public JwtResponse(User user) {
        this.user = user;
    }
    public JwtResponse(String message) {
        this.message = message;
    }
}