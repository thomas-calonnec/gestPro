package com.thomas.gestPro.service;

import com.thomas.gestPro.model.CustomUserDetails;
import com.thomas.gestPro.model.User;
import com.thomas.gestPro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;

@Service
@RequiredArgsConstructor
public class TemporaryUserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from the database by username
        Optional<User> user = userRepository.getUserByUsername(username);

        return user.map(CustomUserDetails::new).orElse(null);
    }
}