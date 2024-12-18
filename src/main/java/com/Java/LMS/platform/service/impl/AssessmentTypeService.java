package com.Java.LMS.platform.service.impl;

import com.Java.LMS.platform.domain.Entities.AssessmentType;
import com.Java.LMS.platform.infrastructure.repository.AssessmentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AssessmentTypeService {

    @Autowired
    private AssessmentTypeRepository assessmentTypeRepository;

    // Method to find an AssessmentType by ID
    public Optional<AssessmentType> getAssessmentTypeById(Long id) {
        return assessmentTypeRepository.findById(id);
    }
}
