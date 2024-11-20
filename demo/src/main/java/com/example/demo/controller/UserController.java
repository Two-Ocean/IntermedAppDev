package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/AssignmentUsers")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping
    public void addUser(@RequestBody User user){
        userService.addUser(user);
    }
    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
    }
    @PutMapping
    public void updateUser(@RequestBody User user){
        userService.updateUser(user);
    }
    @DeleteMapping("/{userID}")
    public void deleteUser(@PathVariable Integer attID){
        userService.deleteUser(attID);
    }
}
