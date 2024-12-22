package com.Java.LMS.platform.service.impl;

import com.Java.LMS.platform.domain.Entities.Assessment;
import com.Java.LMS.platform.infrastructure.repository.AssessmentRepository;
import com.Java.LMS.platform.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//@Transactional
@Service
public class AssessmentServiceImpl implements AssessmentService {

    @Autowired
    private final AssessmentRepository assessmentRepository;

    public AssessmentServiceImpl(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }
    // Get all assessments by course ID
    public List<Assessment> getAssessmentsByCourse(Long courseId) {
        return assessmentRepository.findByCourse_CourseId(courseId);
    }
    public Optional<Assessment> getAssessmentByName(String name) {
        return assessmentRepository.findByName(name);
    }
    public List<Assessment> getAssessmentsByType(Long typeId) {
        return assessmentRepository.findByAssessmentTypeId(typeId);
    }
    public Assessment saveAssessment(Assessment assessment) {
        return assessmentRepository.save(assessment);
    }
    public void deleteAssessment(Long assessmentId) {
        if (!assessmentRepository.existsById(assessmentId)) {
            System.out.println("dsa");
        }
        assessmentRepository.deleteById(assessmentId);
    }
    public Optional<Assessment> findById(Long Id){
        return assessmentRepository.findById(Id);
    }
}
