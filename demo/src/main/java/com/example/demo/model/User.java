package com.example.demo.model;

public class User {
    private Integer userID;
    private String email;
    private String username;
    private String password;
    private Integer points;
    private String role;

    public User() {

    }

    public User(int userID, String email, String username, String password, Integer points, String role) {
        this.userID = userID;
        this.email = email;
        this.username = username;
        this.password = password;
        this.points = points;
        this.role = role;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoints() {
        return points;
    }


    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
