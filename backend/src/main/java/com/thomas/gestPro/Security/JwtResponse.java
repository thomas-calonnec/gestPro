package com.thomas.gestPro.Security;

import com.thomas.gestPro.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponse {
    private  User currentUser;
    private  Boolean isConnected;
    public JwtResponse(User currentUser) {
        this.currentUser = currentUser;
    }
    public JwtResponse(Boolean isConnected) {
        this.isConnected = isConnected;
    }
}