package com.thomas.gestPro.Security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtResponseGoogle {
    // Getters and setters (if needed)
    private Long expiration;

    private String email;

    private String name;

    private String pictureUrl;

    private String googleId;

    private String message;

    // Constructor for success response
    public JwtResponseGoogle(Long expiration, String email, String name, String pictureUrl, String googleId) {
        this.expiration = expiration;
        this.email = email;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.googleId = googleId;
    }

    // Constructor for error response
    public JwtResponseGoogle(String message) {
        this.message = message;
    }

}
