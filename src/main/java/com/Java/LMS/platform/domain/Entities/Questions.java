package com.Java.LMS.platform.domain.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assessment_id", nullable = false)
    @JsonIgnore
    private Assessment assessment;

    @Column(name = "question_text")
    private String question_text;

    @Column(name = "question_type")
    private String question_type;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public String getQuestionText() {
        return question_text;
    }

    public void setQuestionText(String questionText) {
        this.question_text = questionText;
    }

    public String getQuestionType() {
        return question_type;
    }

    public void setQuestionType(String questionType) {
        this.question_type = questionType;
    }
}
