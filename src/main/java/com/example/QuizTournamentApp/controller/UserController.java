package com.example.QuizTournamentApp.controller;

import com.example.QuizTournamentApp.dto.LoginRequest;
import com.example.QuizTournamentApp.model.User;
import com.example.QuizTournamentApp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.QuizTournamentApp.model.Role;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Register a new user
     * @param user User object from the request body
     * @param role Role query parameter (PLAYER or ADMIN)
     * @return Registered user details
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user, @RequestParam Role role) {
        User registeredUser = userService.registerUser(user, role);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * Login a user
     * @param loginRequest LoginRequest DTO with "usernameOrEmail" and "password"
     * @return Success message
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest loginRequest) {
        String loginResponse = userService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("User logged out successfully.");
    }

    /**
     * Get user by username
     * @param username Username path variable
     * @return User details
     */
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> userOpt = userService.findByUsername(username);
        return userOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update user profile
     * @param userId User ID path variable
     * @param updatedUser Updated user details in the request body
     * @return Updated user details
     */
    @PutMapping("/{userId}/update")
    public ResponseEntity<User> updateUserProfile(@PathVariable Long userId, @Valid @RequestBody User updatedUser) {
        User updatedProfile = userService.updateUserProfile(userId, updatedUser);
        return ResponseEntity.ok(updatedProfile);
    }

    /**
     * Reset password for a user
     * @param email Email provided in the query parameter
     * @return Reset password message
     */
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email) {
        String resetResponse = userService.resetPassword(email);
        return ResponseEntity.ok(resetResponse);
    }
    /**
     * Get all Admin users
     */
    @GetMapping("/admins")
    public ResponseEntity<List<User>> getAllAdmins() {
        List<User> admins = userService.getUsersByRole("ADMIN");
        return ResponseEntity.ok(admins);
    }
    /**
     * Get all Player users
     */
    @GetMapping("/players")
    public ResponseEntity<List<User>> getAllPlayers() {
        List<User> players = userService.getUsersByRole("PLAYER");
        return ResponseEntity.ok(players);
    }
}
