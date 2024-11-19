package com.example.QuizTournamentApp.service;

import com.example.QuizTournamentApp.dto.OpenTDBQuestionDTO;
import com.example.QuizTournamentApp.model.QuizTournament;
import com.example.QuizTournamentApp.model.User;
import com.example.QuizTournamentApp.model.UserQuizScore;
import com.example.QuizTournamentApp.repository.QuizTournamentRepository;
import com.example.QuizTournamentApp.repository.UserQuizScoreRepository;
import com.example.QuizTournamentApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.QuizTournamentApp.model.Role;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizTournamentService {

    @Autowired
    private QuizTournamentRepository quizTournamentRepository;

    @Autowired
    private QuestionService questionService; // Service to fetch OpenTDB questions

    @Autowired
    private EmailService emailService; // Service to send email notifications

    @Autowired
    private UserQuizScoreRepository userQuizScoreRepository;

    @Autowired
    private UserRepository userRepository; // Access user data

    /**
     * Admin: Create a new quiz tournament
     */
    public QuizTournament createQuizTournament(QuizTournament quiz) {
        validateQuizData(quiz);

        // Ensure startDate is before endDate
        if (quiz.getStartDate().after(quiz.getEndDate())) {
            throw new IllegalArgumentException("Quiz start date must be before end date");
        }

        // Fetch questions from OpenTDB
        List<OpenTDBQuestionDTO> questions = questionService.fetchQuestionsFromOpenTDB();
        System.out.println("Fetched questions from OpenTDB: " + questions);

        // Save the quiz to the database
        QuizTournament savedQuiz = quizTournamentRepository.save(quiz);

        // Notify all non-admin users about the new quiz
        notifyUsersAboutNewQuiz(savedQuiz);

        return savedQuiz;
    }

    /**
     * Notify all non-admin users (with role PLAYER) about a newly created quiz tournament
     */
    private void notifyUsersAboutNewQuiz(QuizTournament quiz) {
        List<User> players = userRepository.findByRole(Role.PLAYER);

        if (players.isEmpty()) {
            System.out.println("No players found to notify about the new quiz.");
            return;
        }

        for (User player : players) {
            emailService.sendEmail(
                    player.getEmail(),
                    "New Quiz Created: " + quiz.getName(),
                    "Hello " + player.getFirstName() + ",\n\n" +
                            "A new quiz tournament has been created:\n" +
                            "Quiz Name: " + quiz.getName() + "\n" +
                            "Category: " + quiz.getCategory() + "\n" +
                            "Start Date: " + quiz.getStartDate() + "\n" +
                            "End Date: " + quiz.getEndDate() + "\n\n" +
                            "Good luck and happy quizzing!"
            );
        }
    }

    /**
     * Admin & Player: Get all quizzes
     */
    public List<QuizTournament> getAllQuizTournaments() {
        return quizTournamentRepository.findAll();
    }

    /**
     * Admin & Player: Get a specific quiz by ID
     */
    public QuizTournament getQuizById(Long id) {
        return quizTournamentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with id: " + id));
    }

    /**
     * Admin: Update an existing quiz tournament
     */
    public QuizTournament updateQuizTournament(Long id, QuizTournament updatedQuiz) {
        QuizTournament existingQuiz = quizTournamentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with id: " + id));

        // Update fields if they are provided in the request
        if (updatedQuiz.getName() != null) {
            existingQuiz.setName(updatedQuiz.getName());
        }
        if (updatedQuiz.getCategory() != null) {
            existingQuiz.setCategory(updatedQuiz.getCategory());
        }
        if (updatedQuiz.getDifficulty() != null) {
            existingQuiz.setDifficulty(updatedQuiz.getDifficulty());
        }
        if (updatedQuiz.getStartDate() != null) {
            if (updatedQuiz.getEndDate() != null && updatedQuiz.getStartDate().after(updatedQuiz.getEndDate())) {
                throw new IllegalArgumentException("Start date cannot be after end date");
            }
            existingQuiz.setStartDate(updatedQuiz.getStartDate());
        }
        if (updatedQuiz.getEndDate() != null) {
            if (updatedQuiz.getStartDate() != null && updatedQuiz.getStartDate().after(updatedQuiz.getEndDate())) {
                throw new IllegalArgumentException("Start date cannot be after end date");
            }
            existingQuiz.setEndDate(updatedQuiz.getEndDate());
        }

        return quizTournamentRepository.save(existingQuiz);
    }

    /**
     * Edit an existing quiz (Admin feature)
     */
    public QuizTournament editQuiz(Long quizId, QuizTournament updatedQuiz) {
        // Fetch the quiz to update
        QuizTournament existingQuiz = quizTournamentRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with id: " + quizId));

        // Update fields if provided
        if (updatedQuiz.getName() != null) {
            existingQuiz.setName(updatedQuiz.getName());
        }
        if (updatedQuiz.getCategory() != null) {
            existingQuiz.setCategory(updatedQuiz.getCategory());
        }
        if (updatedQuiz.getDifficulty() != null) {
            existingQuiz.setDifficulty(updatedQuiz.getDifficulty());
        }
        if (updatedQuiz.getStartDate() != null) {
            if (updatedQuiz.getEndDate() != null && updatedQuiz.getStartDate().after(updatedQuiz.getEndDate())) {
                throw new IllegalArgumentException("Start date cannot be after end date");
            }
            existingQuiz.setStartDate(updatedQuiz.getStartDate());
        }
        if (updatedQuiz.getEndDate() != null) {
            if (updatedQuiz.getStartDate() != null && updatedQuiz.getStartDate().after(updatedQuiz.getEndDate())) {
                throw new IllegalArgumentException("Start date cannot be after end date");
            }
            existingQuiz.setEndDate(updatedQuiz.getEndDate());
        }

        // Save the updated quiz and return
        return quizTournamentRepository.save(existingQuiz);
    }

    /**
     * Admin: Delete a quiz tournament
     */
    public void deleteQuizTournament(Long id) {
        if (!quizTournamentRepository.existsById(id)) {
            throw new IllegalArgumentException("Quiz Tournament not found with id: " + id);
        }
        quizTournamentRepository.deleteById(id);
    }

    /**
     * Admin & Player: View quizzes by status (ongoing, upcoming, past)
     */
    public List<QuizTournament> getQuizzesByStatus(String status) {
        Date today = new Date();

        switch (status.toLowerCase()) {
            case "ongoing":
                return quizTournamentRepository.findByStartDateBeforeAndEndDateAfter(today, today);
            case "upcoming":
                return quizTournamentRepository.findByStartDateAfter(today);
            case "past":
                return quizTournamentRepository.findByEndDateBefore(today);
            default:
                throw new IllegalArgumentException("Invalid status: " + status + ". Valid options are: ongoing, upcoming, or past.");
        }
    }

    /**
     * Player: Participate in a quiz
     */
    public void participateInQuiz(Long id) {
        QuizTournament quiz = quizTournamentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with id: " + id));

        if (quiz.getParticipants() == null) {
            quiz.setParticipants(new ArrayList<>());
        }
        quiz.getParticipants().add("DefaultPlayerName"); // Replace with actual logic
        quizTournamentRepository.save(quiz);
    }


    /**
     * Player: Like a quiz
     */
    public void likeQuiz(Long id) {
        QuizTournament quiz = quizTournamentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with id: " + id));

        quiz.setLikes(quiz.getLikes() + 1);
        quizTournamentRepository.save(quiz);
    }

    /**
     * Player: Unlike a quiz
     */
    public void unlikeQuiz(Long id) {
        QuizTournament quiz = quizTournamentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with id: " + id));

        if (quiz.getLikes() > 0) {
            quiz.setLikes(quiz.getLikes() - 1);
            quizTournamentRepository.save(quiz);
        } else {
            throw new IllegalStateException("Quiz has no likes to unlike.");
        }
    }

    /**
     * Get the number of likes for a specific quiz
     */
    public int getLikesForQuiz(Long id) {
        QuizTournament quiz = quizTournamentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with id: " + id));

        return quiz.getLikes();
    }

    /**
     * Get the list of participants for a specific quiz
     */
    public List<String> getParticipantsForQuiz(Long id) {
        QuizTournament quiz = quizTournamentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with id: " + id));

        return quiz.getParticipants();
    }

    /**
     * Utility method to validate quiz data
     */
    private void validateQuizData(QuizTournament quiz) {
        if (quiz.getName() == null || quiz.getName().isEmpty()) {
            throw new IllegalArgumentException("Quiz name must not be null or empty.");
        }
        if (quiz.getCategory() == null || quiz.getCategory().isEmpty()) {
            throw new IllegalArgumentException("Quiz category must not be null or empty.");
        }
        if (quiz.getStartDate() == null) {
            throw new IllegalArgumentException("Quiz start date must not be null.");
        }
        if (quiz.getEndDate() == null) {
            throw new IllegalArgumentException("Quiz end date must not be null.");
        }
    }
    /**
     * View scores for a specific quiz tournament, sorted in descending order by score
     */
    public List<UserQuizScore> viewScoresForQuiz(Long quizId) {
        QuizTournament quiz = quizTournamentRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with id: " + quizId));

        // Fetch scores and sort them in descending order
        return userQuizScoreRepository.findByQuiz(quiz).stream()
                .sorted(Comparator.comparingInt(UserQuizScore::getScore).reversed())
                .collect(Collectors.toList());
    }
}
