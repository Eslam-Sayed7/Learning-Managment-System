package com.Java.LMS.platform.domain.Entities;

import jakarta.persistence.*;
import java.util.List;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "assessments")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assessment_id")
    private Long assessmentId;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private AssessmentType assessmentType;
    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Questions> questions;
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "assessment")
    private List<Submission> submissions;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Getters and Setters
    public Long getId() {
        return assessmentId;
    }

    public void setId(Long id) {
        this.assessmentId = id;
    }

    public Course getCourse() {
        return course;
    }
    public List<Questions> getQuestions() {
        return questions;
    }
    public void setCourse(Course course) {
        this.course = course;
    }
    public void setQuestions(List<Questions> questions) {
        this.questions = questions;
    }
    public AssessmentType getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(AssessmentType assessmentType) {
        this.assessmentType = assessmentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
