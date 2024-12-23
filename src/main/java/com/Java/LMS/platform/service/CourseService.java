package com.Java.LMS.platform.service;

import com.Java.LMS.platform.domain.Entities.Course;
import com.Java.LMS.platform.domain.Entities.User;
import com.Java.LMS.platform.service.dto.CourseRequestModel;
import com.Java.LMS.platform.service.dto.EnrollmentRequestModel;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    Course createCourse(CourseRequestModel request);
    List<Course> getAllCourses();
    void enrollStudent(Long courseId, EnrollmentRequestModel enrollmentRequest);
    List<User> getEnrolledStudents(Long courseId);

    Optional<Course> getCourseById(Long courseId);

    Long getUserIdForStudent(Long studentId);
    boolean removeStudentFromCourse(Long courseId, Long studentId);

    boolean deleteCourse(Long courseId);

//    User getStudentById(Long studentId);
}