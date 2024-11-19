package com.example.QuizTournamentApp.service;

import com.example.QuizTournamentApp.model.User;
import com.example.QuizTournamentApp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User.UserBuilder;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from the database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Build UserDetails object using Spring Security's User class
        UserBuilder builder = org.springframework.security.core.userdetails.User.builder();
        builder.username(user.getUsername());
        builder.password(user.getPassword());
        builder.roles(user.getRole().name()); // Role (e.g., ADMIN, PLAYER)

        return builder.build();
    }
}
