package com.Java.LMS.platform.infrastructure.repository;

import com.Java.LMS.platform.domain.Entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course save(Course course);

    List<Course> findAll();

    Optional<Course> findById(Long courseId);

    Optional<Course> getCourseByCourseId(Long courseId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Course c WHERE c.courseId = :courseId")
    void deleteCourseByCourseId(@Param("courseId") Long courseId);
}