package com.example.QuizTournamentApp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for handling login requests.
 */
public class LoginRequest {

    // Username or email for login
    @NotBlank(message = "Username or Email is required")
    @Size(min = 3, max = 50, message = "Username or Email must be between 3 and 50 characters")
    private String usernameOrEmail;

    // Password for login
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters")
    private String password;

    // Getters and Setters
    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // ToString for debugging/logging purposes (exclude sensitive data like password)
    @Override
    public String toString() {
        return "LoginRequest{" +
                "usernameOrEmail='" + usernameOrEmail + '\'' +
                '}';
    }
}
