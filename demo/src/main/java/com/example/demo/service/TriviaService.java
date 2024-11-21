package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TriviaService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> fetchQuestions(int amount, String category, String difficulty, String type) {
        String url = String.format(
                "https://opentdb.com/api.php?amount=%d&category=%s&difficulty=%s&type=%s",
                amount, category, difficulty, type
        );
        return restTemplate.getForObject(url, Map.class);
    }
}
