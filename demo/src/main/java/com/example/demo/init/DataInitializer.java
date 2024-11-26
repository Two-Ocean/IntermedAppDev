package com.example.demo.init;

import com.example.demo.model.User;
import com.example.demo.model.UserType;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        if (userService.findByUsername("admin") == null) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("P@ss123"); // Consider encoding passwords in production
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setEmail("admin@example.com");
            admin.setUserType(UserType.ADMIN);
            userService.createUser(admin);
            System.out.println("Admin user created: username='admin', password='P@ss123'");
        }
    }
}