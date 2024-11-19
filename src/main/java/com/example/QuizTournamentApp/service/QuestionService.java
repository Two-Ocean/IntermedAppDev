package com.example.QuizTournamentApp.service;

import com.example.QuizTournamentApp.dto.OpenTDBQuestionDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    private static final String OPEN_TDB_API_URL = "https://opentdb.com/api.php?amount=10&type=multiple";

    public List<OpenTDBQuestionDTO> fetchQuestionsFromOpenTDB() {
        RestTemplate restTemplate = new RestTemplate();
        List<OpenTDBQuestionDTO> questions = new ArrayList<>();

        try {
            String jsonResponse = restTemplate.getForObject(OPEN_TDB_API_URL, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode results = mapper.readTree(jsonResponse).get("results");

            for (JsonNode result : results) {
                OpenTDBQuestionDTO question = new OpenTDBQuestionDTO();
                question.setQuestion(result.get("question").asText());
                question.setCorrectAnswer(result.get("correct_answer").asText()); // Correct method name

                List<String> incorrectAnswers = new ArrayList<>();
                result.get("incorrect_answers").forEach(answer -> incorrectAnswers.add(answer.asText()));
                question.setIncorrectAnswers(incorrectAnswers); // Correct method name

                questions.add(question);
            }
        } catch (Exception e) {
            System.err.println("Error fetching or parsing questions from OpenTDB: " + e.getMessage());
            e.printStackTrace();
        }

        return questions;
    }
}
