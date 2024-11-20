package com.example.demo.controller;

import com.example.demo.model.Quiz;
import com.example.demo.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class QuizController {
    @Autowired
    private RestTemplate restTemplate;
    @GetMapping("/quiz")
    public String getQuestions(){
        String url ="https://opentdb.com/api.php?amount=2&category=21&difficulty=easy&type=multiple";
        String res = restTemplate.getForObject(url,String.class);
        return res;
    }
    @GetMapping("/quiz_list")
    public List<Question> getQuestionsList(){
        String url ="https://opentdb.com/api.php?amount=2&category=21&difficulty=easy&type=multiple";
        Quiz quiz= restTemplate.getForObject(url, Quiz.class);
        return quiz.getResults();
    }
}
