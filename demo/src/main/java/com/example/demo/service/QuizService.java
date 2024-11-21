package com.example.demo.service;

import com.example.demo.model.Question;
import com.example.demo.model.Quiz;
import com.example.demo.model.User;
import com.example.demo.repos.QuizRepo;
import com.example.demo.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    public Quiz createQuiz(Quiz quiz) {
        Quiz savedQuiz = quizRepo.save(quiz);
        // Notify all non-admin users
        List<User> users = userRepo.findAll();
        users.stream()
                .filter(user -> user.getUserType().name().equalsIgnoreCase("PLAYER"))
                .forEach(user -> emailService.sendEmail(
                        user.getEmail(),
                        "New Quiz Announcement",
                        "A new quiz \"" + savedQuiz.getName() + "\" is available from "
                                + savedQuiz.getStartDate() + " to " + savedQuiz.getEndDate() + "."));
        return savedQuiz;
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepo.findAll();
    }

    public Quiz getQuizById(Integer id) {
        return quizRepo.findById(id).orElse(null);
    }

    public Quiz updateQuiz(Integer id, Quiz updatedQuiz) {
        Optional<Quiz> quizOptional = quizRepo.findById(id);
        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            quiz.setName(updatedQuiz.getName());
            quiz.setStartDate(updatedQuiz.getStartDate());
            quiz.setEndDate(updatedQuiz.getEndDate());
            return quizRepo.save(quiz);
        }
        return null;
    }

    public void deleteQuiz(Integer id) {
        quizRepo.deleteById(id);
    }

    public Quiz addQuestionsToQuiz(Integer id, List<Question> questions) {
        Optional<Quiz> quizOptional = quizRepo.findById(id);
        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            quiz.getQuestions().addAll(questions);
            return quizRepo.save(quiz);
        }
        return null;
    }
}