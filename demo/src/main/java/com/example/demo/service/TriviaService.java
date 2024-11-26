package com.example.demo.service;

import com.example.demo.model.Difficulty;
import com.example.demo.model.Question;
import com.example.demo.repos.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class TriviaService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private QuestionRepo questionRepo; // Ensure this is properly autowired

    public Map<String, Object> fetchQuestions(int amount, String category, String difficulty, String type) {
        String url = String.format(
                "https://opentdb.com/api.php?amount=%d&category=%s&difficulty=%s&type=%s",
                amount, category, difficulty, type
        );
        return restTemplate.getForObject(url, Map.class);
    }

    public void saveTriviaQuestionsToDb(int amount, String category, String difficulty, String type) {
        Map<String, Object> questionsData = fetchQuestions(amount, category, difficulty, type);
        List<Map<String, Object>> questions = (List<Map<String, Object>>) questionsData.get("results");

        for (Map<String, Object> questionData : questions) {
            Question question = new Question();
            question.setQuestion((String) questionData.get("question"));
            question.setCorrectAnswer((String) questionData.get("correct_answer"));
            question.setIncorrectAnswers((List<String>) questionData.get("incorrect_answers"));
            question.setDifficulty(Difficulty.valueOf(((String) questionData.get("difficulty")).toUpperCase()));
            question.setCategory((String) questionData.get("category"));

            // Set the answer field if needed
            question.setAnswer("Sample Answer"); // Replace with appropriate logic

            questionRepo.save(question); // Save to the database
        }
    }

}
