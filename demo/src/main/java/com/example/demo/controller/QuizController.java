package com.example.demo.controller;

import com.example.demo.model.Quiz;
import com.example.demo.model.Question;
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
    @PostMapping
    public Quiz createQuiz(@RequestBody Quiz quiz) {
        return quizService.createQuiz(quiz);
    }

    // Get all quizzes
    @GetMapping
    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    // Get a specific quiz by ID
    @GetMapping("/{id}")
    public Quiz getQuizById(@PathVariable Integer id) {
        return quizService.getQuizById(id);
    }

    // Update a quiz
    @PutMapping("/{id}")
    public Quiz updateQuiz(@PathVariable Integer id, @RequestBody Quiz updatedQuiz) {
        return quizService.updateQuiz(id, updatedQuiz);
    }

    // Delete a quiz
    @DeleteMapping("/{id}")
    public void deleteQuiz(@PathVariable Integer id) {
        quizService.deleteQuiz(id);
    }

    // Add questions to a quiz
    @PostMapping("/{id}/questions")
    public Quiz addQuestionsToQuiz(@PathVariable Integer id, @RequestBody List<Question> questions) {
        return quizService.addQuestionsToQuiz(id, questions);
    }
}