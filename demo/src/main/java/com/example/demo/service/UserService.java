package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public void addUser(User user) {
        userRepo.save(user);
    }
    public List<User> getUsers(){
        List<User> users = new ArrayList<>();
        userRepo.findAll().forEach(users::add);
        return users;
    }
    public void updateUser(User user){
        userRepo.save(user);
    }
    public void deleteUser(@PathVariable Integer userID){
        userRepo.deleteById(userID);
    }

}
