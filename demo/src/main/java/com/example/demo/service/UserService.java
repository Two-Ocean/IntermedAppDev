package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Create a new user
    public User createUser(User user) {
        // Hash the user's password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // Get a user by ID
    public User getUserById(Integer id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }

    // Update user details
    public User updateUser(Integer id, User updatedUser) {
        return userRepo.findById(id).map(user -> {
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());
            user.setBio(updatedUser.getBio());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            return userRepo.save(user);
        }).orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }

    // Delete a user
    public void deleteUser(Integer id) {
        if (!userRepo.existsById(id)) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        userRepo.deleteById(id);
    }

    // Reset user password
    public void resetPassword(Integer id, String newPassword) {
        userRepo.findById(id).ifPresentOrElse(user -> {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepo.save(user);
            try {
                emailService.sendEmail(user.getEmail(), "Password Reset Notification",
                        "Your password has been successfully reset.");
            } catch (Exception e) {
                throw new RuntimeException("Failed to send password reset email", e);
            }
        }, () -> {
            throw new IllegalArgumentException("User not found with ID: " + id);
        });
    }

    // Find user by username
    public User findByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
    }

    // Authenticate user with username and password
    public boolean authenticateUser(String username, String password) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        System.out.println("Provided password: " + password);
        System.out.println("Stored hashed password: " + user.getPassword());
        return passwordEncoder.matches(password, user.getPassword());
    }

    public void hashExistingPasswords() {
        List<User> users = userRepo.findAll();
        for (User user : users) {
            // Check if the password is already hashed (BCrypt passwords always start with $2a$)
            if (!user.getPassword().startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepo.save(user);
            }
        }
    }


}
