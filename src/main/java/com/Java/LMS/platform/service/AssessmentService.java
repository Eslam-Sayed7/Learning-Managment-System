package com.Java.LMS.platform.service;

import com.Java.LMS.platform.domain.Entities.Assessment;

import java.util.List;
import java.util.Optional;

public interface AssessmentService {
    public List<Assessment> getAssessmentsByCourse(Long courseId);
    public Optional<Assessment> getAssessmentByName(String name);
    public List<Assessment> getAssessmentsByType(Long typeId);
    public Assessment saveAssessment(Assessment assessment);
    public void deleteAssessment(Long assessmentId);
    public Optional<Assessment> findById(Long Id);
}
