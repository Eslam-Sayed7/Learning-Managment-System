package com.Java.LMS.platform.domain.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssessmentRequest {
    @JsonProperty("course_id")
    private Long courseId;
    @JsonProperty("type_id")
    private Long assessmentTypeId;
    private String name;
    private String description;

    // Getters and Setters
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getAssessmentTypeId() {
        return assessmentTypeId;
    }

    public void setAssessmentTypeId(Long assessmentTypeId) {
        this.assessmentTypeId = assessmentTypeId;
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
}
