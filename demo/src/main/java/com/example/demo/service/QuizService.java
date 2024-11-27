package com.example.demo.service;

import com.example.demo.dto.QuestionDTO;
import com.example.demo.dto.QuizDTO;
import com.example.demo.model.*;
import com.example.demo.repos.QuestionRepo;
import com.example.demo.repos.QuizRepo;
import com.example.demo.repos.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    private QuestionRepo questionRepo;

    @Autowired
    private QuizRepo quizRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    public QuizDTO convertToDTO(Quiz quiz) {
        List<QuestionDTO> questionDTOs = quiz.getQuestions().stream()
                .map(q -> new QuestionDTO(q.getQuestionID(), q.getQuestion(), q.getDifficulty().name(), q.getCategory(), q.getCorrectAnswer()))
                .collect(Collectors.toList());

        return new QuizDTO(quiz.getQuizTitle(), questionDTOs);
    }


    @Transactional
    public Quiz createQuiz(Quiz quiz, Integer userId) {
        // Validate user
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getUserType().equals(UserType.ADMIN)) {
            throw new IllegalArgumentException("Only ADMIN users can create quizzes.");
        }

        // Validate and map questions
        List<Question> questions = quiz.getQuestions().stream()
                .map(question -> {
                    // Validate question ID
                    if (question.getQuestionID() == null) {
                        throw new IllegalArgumentException("Question ID must not be null.");
                    }

                    // Fetch question from DB
                    Question existingQuestion = questionRepo.findById(question.getQuestionID())
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "Question not found with ID: " + question.getQuestionID()));

                    // Map Difficulty case-insensitively
                    if (question.getDifficulty() != null) {
                        existingQuestion.setDifficulty(Difficulty.fromString(question.getDifficulty().name()));
                    }

                    existingQuestion.setQuiz(quiz); // Associate question with quiz
                    return existingQuestion;
                })
                .collect(Collectors.toList());

        quiz.setQuestions(questions); // Set validated questions
        quiz.setCreatedBy(user.getUserID());
        return quizRepo.save(quiz); // Save the quiz
    }






    // Get all quizzes
    public List<Quiz> getAllQuizzes() {
        return quizRepo.findAll();
    }

    // Get a quiz by ID
    public Quiz getQuizById(Integer quizId) {
        return quizRepo.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));
    }

    // Participate in a quiz
    public Quiz participateInQuiz(Integer quizId, Integer userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getUserType().equals(UserType.PLAYER)) {
            throw new IllegalArgumentException("Only PLAYER users can participate in quizzes.");
        }

        Quiz quiz = getQuizById(quizId);

        if (quiz.getStartDate().isAfter(java.time.LocalDate.now()) ||
                quiz.getEndDate().isBefore(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("Quiz is not currently active.");
        }

        return quiz;
    }

    // Update a quiz
    public Quiz updateQuiz(Integer quizId, Quiz updatedQuiz) {
        Quiz existingQuiz = getQuizById(quizId);

        // Update fields
        existingQuiz.setQuizTitle(updatedQuiz.getQuizTitle());
        existingQuiz.setStartDate(updatedQuiz.getStartDate());
        existingQuiz.setEndDate(updatedQuiz.getEndDate());

        return quizRepo.save(existingQuiz);
    }

    // Delete a quiz
    public String deleteQuiz(Integer quizId) {
        Quiz quiz = getQuizById(quizId);
        quizRepo.delete(quiz);
        return "Quiz deleted successfully: " + quiz.getQuizTitle();
    }

    // Get scores sorted for a quiz
    public List<Map<String, Object>> getScoresSorted(Integer quizId) {
        Quiz quiz = getQuizById(quizId);

        // Create a list of maps with questionId and score
        return quiz.getQuestions()
                .stream()
                .map(question -> {
                    Map<String, Object> questionScoreMap = new HashMap<>();
                    questionScoreMap.put("questionId", question.getQuestionID());
                    questionScoreMap.put("score", calculateScoreForQuestion(question));
                    return questionScoreMap;
                })
                .sorted((map1, map2) -> Integer.compare((Integer) map2.get("score"), (Integer) map1.get("score")))
                .collect(Collectors.toList());
    }

    private Integer calculateScoreForQuestion(Question question) {
        // Example scoring logic: Assign scores based on difficulty
        if (question.getDifficulty() == null) {
            return 0; // Default score for null difficulty
        }
        switch (question.getDifficulty()) {
            case EASY:
                return 1;
            case MEDIUM:
                return 2;
            case HARD:
                return 3;
            default:
                return 0; // Fallback score
        }
    }


    // Submit an answer
    public Map<String, Object> submitAnswer(Integer quizId, Integer userId, Integer questionId, String answer) {
        Quiz quiz = getQuizById(quizId);
        Question question = questionRepo.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));

        Map<String, Object> feedback = new HashMap<>();
        if (question.getCorrectAnswer().equalsIgnoreCase(answer)) {
            feedback.put("correct", true);
        } else {
            feedback.put("correct", false);
            feedback.put("correctAnswer", question.getCorrectAnswer());
        }

        // Example: Add logic to update user score if necessary

        return feedback;
    }
}
