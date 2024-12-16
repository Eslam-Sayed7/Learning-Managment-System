package com.Java.LMS.platform.infrastructure.repository;

import com.Java.LMS.platform.domain.Entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    Lesson save(Lesson lesson);

    Optional<Lesson> findById(Long lessonId);
//    Optional<Lesson> findByCourseIdAndLessonName(Long courseId, String lessonName);

    @Query("SELECT l FROM Lesson l WHERE l.course_id = :courseId AND l.lessonName = :lessonName")
    Optional<Lesson> findLessonByCourseIdAndName(@Param("courseId") Long courseId, @Param("lessonName") String lessonName);
}
