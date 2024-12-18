package com.Java.LMS.platform.infrastructure.repository;

import com.Java.LMS.platform.domain.Entities.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    // Custom queries if needed

    // Find assessments by course
    List<Assessment> findByCourse_CourseId(Long courseId); // course.courseId should match the property in Course entity

    // Find assessment by name
    Optional<Assessment> findByName(String name);

    // Find assessments by assessment type
    List<Assessment> findByAssessmentTypeId(Long typeId);

    //find Assessment By id
    Optional<Assessment> findById(Long Id);
}
