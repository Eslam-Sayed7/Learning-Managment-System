package com.Java.LMS.platform.service.impl;

import com.Java.LMS.platform.domain.Entities.Assessment;
import com.Java.LMS.platform.infrastructure.repository.AssessmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    // Get all assessments by course ID
    public List<Assessment> getAssessmentsByCourse(Long courseId) {
        return assessmentRepository.findByCourse_CourseId(courseId);
    }

    // Get assessment by name
    public Optional<Assessment> getAssessmentByName(String name) {
        return assessmentRepository.findByName(name);
    }

    // Get assessments by type
    public List<Assessment> getAssessmentsByType(Long typeId) {
        return assessmentRepository.findByAssessmentTypeId(typeId);
    }

    // Save an assessment
    public Assessment saveAssessment(Assessment assessment) {
        return assessmentRepository.save(assessment);
    }

    // Delete an assessment by ID
    public void deleteAssessment(Long assessmentId) {
        if (!assessmentRepository.existsById(assessmentId)) {
            System.out.println("dsa");
        }
        assessmentRepository.deleteById(assessmentId);
    }
    // Find Assessment By id
    public Optional<Assessment> findById(Long Id){
        return assessmentRepository.findById(Id);
    }
}
