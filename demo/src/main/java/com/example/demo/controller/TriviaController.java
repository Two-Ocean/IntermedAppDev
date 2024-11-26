package com.example.demo.controller;

import com.example.demo.service.TriviaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trivia")
public class TriviaController {

    @Autowired
    private TriviaService triviaService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/questions")
    public Map<String, Object> getQuestions(
            @RequestParam(defaultValue = "10") int amount,
            @RequestParam(defaultValue = "") String category,
            @RequestParam(defaultValue = "") String difficulty,
            @RequestParam(defaultValue = "multiple") String type
    ) {
        return triviaService.fetchQuestions(amount, category, difficulty, type);
    }
    @GetMapping("/categories")
    public List<Map<String, Object>> getCategories() {
        String url = "https://opentdb.com/api_category.php";
        return (List<Map<String, Object>>) restTemplate.getForObject(url, Map.class).get("trivia_categories");
    }

    @GetMapping("/save-questions")
    public String saveQuestions(
            @RequestParam(defaultValue = "10") int amount,
            @RequestParam(defaultValue = "") String category,
            @RequestParam(defaultValue = "") String difficulty,
            @RequestParam(defaultValue = "multiple") String type
    ) {
        triviaService.saveTriviaQuestionsToDb(amount, category, difficulty, type);
        return "Questions saved to the database";
    }
}
