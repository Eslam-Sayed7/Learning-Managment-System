package com.Java.LMS.platform.infrastructure.repository;

import com.Java.LMS.platform.domain.Entities.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    // Fetch attendance by lesson ID
    List<Attendance> findByLessonId(Long lessonId);

    // Fetch attendance by student ID
    List<Attendance> findByStudentId(Long studentId);

    // Check if an attendance record exists for a specific lesson and student
    boolean existsByLessonIdAndStudentId(Long lessonId, Long studentId);
}