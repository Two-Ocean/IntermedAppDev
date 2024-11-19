package com.example.QuizTournamentApp.service;

import com.example.QuizTournamentApp.dto.LoginRequest;
import com.example.QuizTournamentApp.model.Role;
import com.example.QuizTournamentApp.model.User;
import com.example.QuizTournamentApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Register a new user with a specific role.
     * Validates uniqueness of username and email.
     */
    public User registerUser(User user, Role role) {
        // Check if the username already exists
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Check if the email already exists
        Optional<User> existingEmailUser = userRepository.findByEmail(user.getEmail());
        if (existingEmailUser.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Validate that the password is not null or empty
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        // Encrypt the password using BCrypt
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign the provided role (e.g., ADMIN or PLAYER)
        user.setRole(role);

        // Save the user to the database
        return userRepository.save(user);
    }

    /**
     * Login logic for users.
     * Validates username/email and password.
     */
    public String login(LoginRequest loginRequest) {
        // Check if the user exists using username or email
        Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsernameOrEmail());
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOpt.get();

        // Verify the password using BCrypt
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return "Login successful for user: " + user.getUsername();
    }

    /**
     * Find a user by username.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Update user profile.
     * Allows updating fields like username, email, firstName, lastName, etc.
     */
    public User updateUserProfile(Long userId, User updatedUser) {
        // Retrieve the existing user
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Update fields only if new values are provided
        if (updatedUser.getFirstName() != null) {
            existingUser.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null) {
            existingUser.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getEmail() != null) {
            // Ensure email is unique
            Optional<User> emailUser = userRepository.findByEmail(updatedUser.getEmail());
            if (emailUser.isPresent() && !emailUser.get().getId().equals(userId)) {
                throw new IllegalArgumentException("Email already exists");
            }
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getUsername() != null) {
            // Ensure username is unique
            Optional<User> usernameUser = userRepository.findByUsername(updatedUser.getUsername());
            if (usernameUser.isPresent() && !usernameUser.get().getId().equals(userId)) {
                throw new IllegalArgumentException("Username already exists");
            }
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getProfilePicture() != null) {
            existingUser.setProfilePicture(updatedUser.getProfilePicture());
        }

        return userRepository.save(existingUser);
    }

    /**
     * Reset a user's password.
     * Generates and assigns a temporary password.
     */
    public String resetPassword(String email) {
        // Check if the user exists by email
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("No user found with the provided email");
        }

        User user = userOpt.get();

        // Generate a temporary password
        String tempPassword = "Temp@12345";

        // Encrypt the temporary password and update it in the database
        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);

        // Return the temporary password (in real systems, email it instead)
        return "Temporary password sent to email: " + email + ". Temporary Password: " + tempPassword;
    }
    /**
     * Fetch users by role (ADMIN or PLAYER)
     */
    public List<User> getUsersByRole(String role) {
        Role userRole = Role.valueOf(role); // Convert the role string to an enum
        return userRepository.findByRole(userRole);
    }
}
