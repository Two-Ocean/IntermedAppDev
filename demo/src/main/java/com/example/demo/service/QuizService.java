package com.example.demo.service;

import com.example.demo.model.Quiz;
import com.example.demo.model.User;
import com.example.demo.model.UserType;
import com.example.demo.repos.QuizRepo;
import com.example.demo.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    public Quiz createQuiz(Quiz quiz, Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getUserType().equals(UserType.ADMIN)) {
            throw new IllegalArgumentException("Only ADMIN users can create quizzes.");
        }

        Quiz savedQuiz = quizRepo.save(quiz);

        // Notify all non-admin users
        List<User> users = userRepo.findAll();
        users.stream()
                .filter(u -> u.getUserType().equals(UserType.PLAYER)) // Exclude ADMIN users
                .forEach(u -> emailService.sendEmail(
                        u.getEmail(),
                        "New Quiz Announcement",
                        "A new quiz \"" + savedQuiz.getName() + "\" is available from "
                                + savedQuiz.getStartDate() + " to " + savedQuiz.getEndDate() + "."));
        return savedQuiz;
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepo.findAll();
    }

    public Quiz participateInQuiz(Integer quizId, Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getUserType().equals(UserType.PLAYER)) {
            throw new IllegalArgumentException("Only PLAYER users can participate in quizzes.");
        }

        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        if (quiz.getStartDate().isAfter(java.time.LocalDate.now()) ||
                quiz.getEndDate().isBefore(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("Quiz is not currently active.");
        }

        return quiz;
    }
    public Quiz updateQuiz(Integer quizId, Quiz updatedQuiz) {
        Quiz existingQuiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        // Update fields
        existingQuiz.setName(updatedQuiz.getName());
        existingQuiz.setStartDate(updatedQuiz.getStartDate());
        existingQuiz.setEndDate(updatedQuiz.getEndDate());

        return quizRepo.save(existingQuiz);
    }
    public String deleteQuiz(Integer quizId) {
        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        quizRepo.delete(quiz);
        return "Quiz deleted successfully: " + quiz.getName();
    }
}