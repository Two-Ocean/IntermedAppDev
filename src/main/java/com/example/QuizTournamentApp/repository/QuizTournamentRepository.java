package com.example.QuizTournamentApp.repository;

import com.example.QuizTournamentApp.model.QuizTournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface QuizTournamentRepository extends JpaRepository<QuizTournament, Long> {
    // Fetch ongoing quizzes (startDate <= today AND endDate >= today)
    List<QuizTournament> findByStartDateBeforeAndEndDateAfter(Date startDate, Date endDate);

    // Fetch upcoming quizzes (startDate > today)
    List<QuizTournament> findByStartDateAfter(Date startDate);

    // Fetch past quizzes (endDate < today)
    List<QuizTournament> findByEndDateBefore(Date endDate);
}
