package com.Java.LMS.platform.service.impl;

import com.Java.LMS.platform.domain.Entities.Assessment;
import com.Java.LMS.platform.domain.Entities.Questions;
import com.Java.LMS.platform.infrastructure.repository.AssessmentRepository;
import com.Java.LMS.platform.infrastructure.repository.QuestionRepository;
import com.Java.LMS.platform.service.dto.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;
    public List<Questions> getRandomQuestions(Long assessmentId) {
        // Fetch all questions for the assessment
        List<Questions> questions = questionRepository.findByAssessmentId(assessmentId);

        Collections.shuffle(questions);

        return questions;
    }
    public Questions addQuestionToAssessment(Long assessmentId, String questionText, String questionType) {
        // Fetch the assessment
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with ID: " + assessmentId));

        // Create the question
        System.out.println(questionText);
        Questions question = new Questions();
        question.setAssessment(assessment);
        question.setQuestionText(questionText);
        question.setQuestionType(questionType);

        // Save the question
        return questionRepository.save(question);
    }
    public List<Questions> findAllQuestionsById(Long assessmentId){
        return questionRepository.findByAssessmentId(assessmentId);
    }

}
