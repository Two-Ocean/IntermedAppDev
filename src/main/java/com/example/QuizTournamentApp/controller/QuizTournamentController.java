package com.example.QuizTournamentApp.controller;

import com.example.QuizTournamentApp.dto.OpenTDBQuestionDTO;
import com.example.QuizTournamentApp.model.QuizTournament;
import com.example.QuizTournamentApp.model.UserQuizScore;
import com.example.QuizTournamentApp.service.QuestionService;
import com.example.QuizTournamentApp.service.QuizTournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizTournamentController {

    @Autowired
    private QuizTournamentService quizTournamentService;

    @Autowired
    private QuestionService questionService; // For fetching questions

    /**
     * Admin: Create a new quiz tournament
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<QuizTournament> createQuizTournament(@RequestBody QuizTournament quizTournament) {
        QuizTournament newQuiz = quizTournamentService.createQuizTournament(quizTournament);
        return ResponseEntity.ok(newQuiz);
    }

    /**
     * Admin & Player: Get all quizzes
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PLAYER')")
    @GetMapping("/all")
    public ResponseEntity<List<QuizTournament>> getAllQuizTournaments() {
        List<QuizTournament> quizzes = quizTournamentService.getAllQuizTournaments();
        return ResponseEntity.ok(quizzes);
    }

    /**
     * Admin: Delete a quiz tournament
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteQuizTournament(@PathVariable Long id) {
        quizTournamentService.deleteQuizTournament(id);
        return ResponseEntity.ok("Quiz tournament deleted successfully!");
    }

    /**
     * Admin & Player: Get quizzes by status (ongoing, upcoming, past)
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PLAYER')")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<QuizTournament>> getQuizzesByStatus(@PathVariable String status) {
        List<QuizTournament> quizzes = quizTournamentService.getQuizzesByStatus(status);
        return ResponseEntity.ok(quizzes);
    }

    /**
     * Player: Participate in a quiz
     */
    @PreAuthorize("hasRole('PLAYER')")
    @PostMapping("/{id}/participate")
    public ResponseEntity<String> participateInQuiz(@PathVariable Long id) {
        quizTournamentService.participateInQuiz(id);
        return ResponseEntity.ok("Successfully participated in the quiz!");
    }



    /**
     * Player: Like a quiz
     */
    @PreAuthorize("hasRole('PLAYER')")
    @PostMapping("/{id}/like")
    public ResponseEntity<String> likeQuiz(@PathVariable Long id) {
        quizTournamentService.likeQuiz(id);
        return ResponseEntity.ok("Quiz liked successfully!");
    }

    /**
     * Player: Unlike a quiz
     */
    @PreAuthorize("hasRole('PLAYER')")
    @PostMapping("/{id}/unlike")
    public ResponseEntity<String> unlikeQuiz(@PathVariable Long id) {
        quizTournamentService.unlikeQuiz(id);
        return ResponseEntity.ok("Quiz unliked successfully!");
    }

    /**
     * Fetch 10 questions from OpenTDB
     */
    @GetMapping("/questions")
    public ResponseEntity<List<OpenTDBQuestionDTO>> getQuestionsFromOpenTDB() {
        List<OpenTDBQuestionDTO> questions = questionService.fetchQuestionsFromOpenTDB();
        return ResponseEntity.ok(questions);
    }

    /**
     * Get details of a single quiz tournament by ID
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PLAYER')")
    @GetMapping("/{id}")
    public ResponseEntity<QuizTournament> getQuizById(@PathVariable Long id) {
        QuizTournament quiz = quizTournamentService.getQuizById(id);
        return ResponseEntity.ok(quiz);
    }

    /**
     * Update a quiz tournament (Admin Only)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/update")
    public ResponseEntity<QuizTournament> updateQuizTournament(@PathVariable Long id, @RequestBody QuizTournament updatedQuiz) {
        QuizTournament updatedQuizTournament = quizTournamentService.updateQuizTournament(id, updatedQuiz);
        return ResponseEntity.ok(updatedQuizTournament);
    }

    /**
     * Get total likes for a specific quiz
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PLAYER')")
    @GetMapping("/{id}/likes")
    public ResponseEntity<Integer> getLikesForQuiz(@PathVariable Long id) {
        int likes = quizTournamentService.getLikesForQuiz(id);
        return ResponseEntity.ok(likes);
    }

    /**
     * Get participants for a quiz (Admin Only)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/participants")
    public ResponseEntity<List<String>> getParticipantsForQuiz(@PathVariable Long id) {
        List<String> participants = quizTournamentService.getParticipantsForQuiz(id);
        return ResponseEntity.ok(participants);
    }
    /**
     * View scores for a specific quiz, sorted in descending order.
     */
    @GetMapping("/{id}/scores")
    public ResponseEntity<List<UserQuizScore>> viewScores(@PathVariable Long id) {
        List<UserQuizScore> scores = quizTournamentService.viewScoresForQuiz(id);
        return ResponseEntity.ok(scores);
    }
    // PUT Mapping for updating a quiz
    @PutMapping("/{id}/edit")
    public ResponseEntity<QuizTournament> editQuiz(
            @PathVariable Long id,
            @RequestBody QuizTournament updatedQuiz) {
        QuizTournament editedQuiz = quizTournamentService.editQuiz(id, updatedQuiz);
        return ResponseEntity.ok(editedQuiz);
    }

}
