package com.Java.LMS.platform.infrastructure.repository;

import com.Java.LMS.platform.domain.Entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Lesson save(Lesson lesson);

    Optional<Lesson> findById(Long lessonId);
//    Optional<Lesson> findByCourseIdAndLessonName(Long courseId, String lessonName);

    @Query("SELECT l FROM Lesson l WHERE l.courseId = :courseId AND l.lessonName = :lessonName")
    Optional<Lesson> findLessonByCourseIdAndName(@Param("courseId") Long courseId, @Param("lessonName") String lessonName);
    // Query to count total lessons for all courses the student is enrolled in
    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.courseId IN " +
            "(SELECT e.courseId FROM Enrollment e WHERE e.studentId = :studentId)")
    long countTotalLessonsForStudent(@Param("studentId") Long studentId);
    List<Lesson> findLessonsByCourseId(Long courseId);
}
