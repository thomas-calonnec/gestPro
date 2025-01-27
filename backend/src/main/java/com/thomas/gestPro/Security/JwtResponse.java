package com.thomas.gestPro.Security;

import com.thomas.gestPro.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
    private  User user;
    private  Boolean isConnected;

    public JwtResponse(User user) {
        this.user = user;
    }
    public JwtResponse(Boolean isConnected) {
        this.isConnected = isConnected;
    }

}