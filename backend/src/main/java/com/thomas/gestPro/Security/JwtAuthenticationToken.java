package com.thomas.gestPro.Security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;
    private final String username;

    public JwtAuthenticationToken(String token, String username) {
        super(Collections.singletonList(new SimpleGrantedAuthority("USER"))); // Authority based on token
        this.token = token;
        this.username = username;
        setAuthenticated(true); // This is optional, based on your logic
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }
}