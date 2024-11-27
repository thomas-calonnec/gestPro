package com.thomas.gestPro.Security;

import lombok.Getter;

@Getter
public class JwtResponse {
    private final String accessToken;
    public JwtResponse(String accessToken) {
        this.accessToken = accessToken;

    }
}