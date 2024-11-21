package com.example.demo.controller;

import com.example.demo.model.Quiz;
import com.example.demo.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    // Create a new quiz
    @PostMapping("/{userId}")
    public Quiz createQuiz(@RequestBody Quiz quiz, @PathVariable Integer userId) {
        return quizService.createQuiz(quiz, userId);
    }

    // Get all quizzes
    @GetMapping
    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    // Participate in a quiz
    @PostMapping("/{quizId}/participate/{userId}")
    public Quiz participateInQuiz(@PathVariable Integer quizId, @PathVariable Integer userId) {
        return quizService.participateInQuiz(quizId, userId);
    }

    // Update a quiz tournament
    @PutMapping("/{quizId}")
    public Quiz updateQuiz(@PathVariable Integer quizId, @RequestBody Quiz updatedQuiz) {
        return quizService.updateQuiz(quizId, updatedQuiz);
    }
    @DeleteMapping("/{quizId}")
    public String deleteQuiz(@PathVariable Integer quizId) {
        return quizService.deleteQuiz(quizId);
    }
}