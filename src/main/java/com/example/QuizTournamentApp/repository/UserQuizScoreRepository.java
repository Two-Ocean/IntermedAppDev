package com.example.QuizTournamentApp.repository;

import com.example.QuizTournamentApp.model.UserQuizScore;
import com.example.QuizTournamentApp.model.QuizTournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserQuizScoreRepository extends JpaRepository<UserQuizScore, Long> {

    List<UserQuizScore> findByQuiz(QuizTournament quiz);
}
