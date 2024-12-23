package com.Java.LMS.platform.domain.Entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submissionId;

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private String filePath;

    private LocalDateTime submissionDate = LocalDateTime.now();

    private double grade;

    private String feedback;

    public Long getSubmissionId() {
        return submissionId;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public Student getStudent() {
        return student;
    }

    public String getFilePath() {
        return filePath;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public void setId(long l) {
        submissionId = l;
    }
    // getters and setters

}