package com.Java.LMS.platform.domain.Entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "assessment_types")
public class AssessmentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assessment_type_id")
    private Long id;

    @Column(name = "type_name")
    private String typeName;

//    // Relationship with Assessments
//    @OneToMany(mappedBy = "assessmentType", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Assessment> assessments;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

//    public List<Assessment> getAssessments() {
//        return assessments;
//    }
//
//    public void setAssessments(List<Assessment> assessments) {
//        this.assessments = assessments;
//    }
}
