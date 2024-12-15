package com.Java.LMS.platform.service;

import com.Java.LMS.platform.domain.Entities.Course;
import com.Java.LMS.platform.service.dto.CourseRequestModel;
import com.Java.LMS.platform.service.dto.EnrollmentRequestModel;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    Course createCourse(CourseRequestModel request);
    List<Course> getAllCourses();
    void enrollStudent(Long courseId, EnrollmentRequestModel enrollmentRequest);
    List<String> getEnrolledStudents(Long courseId);

    Optional<Course> getCourseById(Long courseId);
}