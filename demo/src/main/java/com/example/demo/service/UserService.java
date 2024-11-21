package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    public User createUser(User user) {
        return userRepo.save(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getUserById(Integer id) {
        return userRepo.findById(id).orElse(null);
    }

    public User updateUser(Integer id, User updatedUser) {
        return userRepo.findById(id).map(user -> {
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());
            user.setBio(updatedUser.getBio());
            user.setPhoneNumber(updatedUser.getPhoneNumber());
            return userRepo.save(user);
        }).orElse(null);
    }

    public void deleteUser(Integer id) {
        userRepo.deleteById(id);
    }

    public void resetPassword(Integer id, String newPassword) {
        userRepo.findById(id).ifPresent(user -> {
            user.setPassword(newPassword);
            userRepo.save(user);
            // Send email notification
            emailService.sendEmail(user.getEmail(), "Password Reset Notification",
                    "Your password has been successfully reset.");
        });
    }
}