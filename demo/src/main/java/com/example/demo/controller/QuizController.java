package com.example.demo.controller;

import com.example.demo.dto.QuizDTO;
import com.example.demo.model.Quiz;
import com.example.demo.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    @PostMapping("/{userId}")
    public ResponseEntity<?> createQuiz(@RequestBody Quiz quiz, @PathVariable Integer userId) {
        try {
            Quiz createdQuiz = quizService.createQuiz(quiz, userId);
            QuizDTO quizDTO = quizService.convertToDTO(createdQuiz);
            return ResponseEntity.ok(quizDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }



    // Get a specific quiz by ID
    @GetMapping("/{quizId}")
    public Quiz getQuizById(@PathVariable Integer quizId) {
        return quizService.getQuizById(quizId);
    }

    // Get all quizzes
    @GetMapping()
    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    // Update a quiz
    @PutMapping("/{quizId}")
    public Quiz updateQuiz(@PathVariable Integer quizId, @RequestBody Quiz updatedQuiz) {
        return quizService.updateQuiz(quizId, updatedQuiz);
    }

    // Delete a quiz
    @DeleteMapping("/{quizId}")
    public ResponseEntity<String> deleteQuiz(@PathVariable Integer quizId) {
        quizService.deleteQuiz(quizId);
        return ResponseEntity.ok("Quiz deleted successfully");
    }

    // Participate in a quiz
    @PostMapping("/{quizId}/participate/{userId}")
    public Quiz participateInQuiz(@PathVariable Integer quizId, @PathVariable Integer userId) {
        return quizService.participateInQuiz(quizId, userId);
    }

    // Submit an answer
    @PostMapping("/{quizId}/questions/{questionId}/submit")
    public ResponseEntity<Map<String, Object>> submitAnswer(
            @PathVariable Integer quizId,
            @PathVariable Integer questionId,
            @RequestBody Map<String, String> requestBody) {
        String answer = requestBody.get("answer");
        Map<String, Object> feedback = quizService.submitAnswer(quizId, null, questionId, answer);
        return ResponseEntity.ok(feedback);
    }

    // Get sorted scores for a quiz
    @GetMapping("/{quizId}/scores")
    public ResponseEntity<List<Object>> getSortedScores(@PathVariable Integer quizId) {
        return ResponseEntity.ok(Collections.singletonList(quizService.getScoresSorted(quizId)));
    }
}
