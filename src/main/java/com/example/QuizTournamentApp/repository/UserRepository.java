package com.example.QuizTournamentApp.repository;

import com.example.QuizTournamentApp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import com.example.QuizTournamentApp.model.Role;

public interface UserRepository extends JpaRepository<User, Long> {

    // Corrected method: Accepts Role enum instead of String
    List<User> findByRole(Role role);
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
