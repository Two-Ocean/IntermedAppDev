package com.example.demo.service;

import com.example.demo.model.Question;
import com.example.demo.repos.QuestionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepo questionRepo;

    public Question createQuestion(Question question) {
        return questionRepo.save(question);
    }

    public List<Question> getAllQuestions() {
        return (List<Question>) questionRepo.findAll();
    }

    public Question getQuestionById(Integer id) {
        return questionRepo.findById(id).orElse(null);
    }

    public Question updateQuestion(Integer id, Question updatedQuestion) {
        return questionRepo.findById(id).map(question -> {
            question.setQuestion(updatedQuestion.getQuestion());
            question.setCorrectAnswer(updatedQuestion.getCorrectAnswer());
            question.setIncorrectAnswers(updatedQuestion.getIncorrectAnswers());
            question.setCategory(updatedQuestion.getCategory());
            question.setDifficulty(updatedQuestion.getDifficulty());
            return questionRepo.save(question);
        }).orElse(null);
    }

    public void deleteQuestion(Integer id) {
        questionRepo.deleteById(id);
    }
}