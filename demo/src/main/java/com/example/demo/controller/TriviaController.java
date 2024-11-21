package com.example.demo.controller;

import com.example.demo.service.TriviaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/trivia")
public class TriviaController {

    @Autowired
    private TriviaService triviaService;

    @GetMapping("/questions")
    public Map<String, Object> getQuestions(
            @RequestParam(defaultValue = "10") int amount,
            @RequestParam(defaultValue = "") String category,
            @RequestParam(defaultValue = "") String difficulty,
            @RequestParam(defaultValue = "multiple") String type
    ) {
        return triviaService.fetchQuestions(amount, category, difficulty, type);
    }
}
