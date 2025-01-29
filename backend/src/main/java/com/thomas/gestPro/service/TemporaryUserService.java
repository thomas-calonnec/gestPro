package com.thomas.gestPro.service;

import com.thomas.gestPro.model.CustomUserDetails;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;

@Service
public class TemporaryUserService implements UserDetailsService {

    private final UserRepository userRepository;

    public TemporaryUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from the database by username
        User user = userRepository.getUserByUsername(username);


        // Convert `User` entity to `CustomUserDetails`
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
    }
}