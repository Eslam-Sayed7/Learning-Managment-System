package com.Java.LMS.platform.infrastructure.repository;

import com.Java.LMS.platform.domain.Entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // Check if a student is already enrolled in a course
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    // Find all enrollments for a specific course
    List<Enrollment> findByCourseId(Long courseId);

    // Find all enrollments for a specific student
    List<Enrollment> findByStudentId(Long studentId);
}
