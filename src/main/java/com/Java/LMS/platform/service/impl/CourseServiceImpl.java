package com.Java.LMS.platform.service.impl;

import com.Java.LMS.platform.domain.Entities.Course;
import com.Java.LMS.platform.domain.Entities.Enrollment;
import com.Java.LMS.platform.domain.Entities.Student;
import com.Java.LMS.platform.domain.Entities.User;
import com.Java.LMS.platform.infrastructure.repository.CourseRepository;
import com.Java.LMS.platform.infrastructure.repository.EnrollmentRepository;
import com.Java.LMS.platform.infrastructure.repository.StudentRepository;
import com.Java.LMS.platform.infrastructure.repository.UserRepository;
import com.Java.LMS.platform.service.CourseService;
import com.Java.LMS.platform.service.dto.CourseRequestModel;
import com.Java.LMS.platform.service.dto.EnrollmentRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private  CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository ;

    @Override
    public Course createCourse(CourseRequestModel request) {
        // Validate input data
        if (request.getTitle() == null || request.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Course title is required");
        }

        // Create and save course
        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setInstructorId(request.getGetInstructor_id());
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public void enrollStudent(Long courseId, EnrollmentRequestModel enrollmentRequest) {
        // Check if course exists
        Optional<Course> course = courseRepository.findById(courseId);

        if (course == null){
            throw new IllegalStateException("Course not found");
        }

        // Check if student exists
        User student = userRepository.findById(enrollmentRequest.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        // Check if student is already enrolled
        boolean isEnrolled = enrollmentRepository.existsByStudentIdAndCourseId(
                enrollmentRequest.getStudentId(), courseId);
        if (isEnrolled) {
            throw new IllegalStateException("Student already enrolled in the course");
        }

        // Create and save enrollment record
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(enrollmentRequest.getStudentId());
        enrollment.setCourseId(courseId);
        enrollment.setEnrollmentDate(enrollmentRequest.getEnrollment_date() != null ? enrollmentRequest.getEnrollment_date() : LocalDateTime.now());
        enrollmentRepository.save(enrollment);
    }
    // Fetch enrolled students and return usernames
    public List<User> getEnrolledStudents(Long courseId) {
        // Fetch enrollments for the course
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);

        // Map enrollments to User objects
        return enrollments.stream()
                .map(enrollment -> {
                    // Fetch the student by student_id
                    Optional<Student> student = studentRepository.findById(enrollment.getStudentId());
                    // Fetch and return the user tied to the student
                    return student.flatMap(s -> userRepository.findById(s.getUserId()))
                            .orElse(null); // Return null if the user is not found
                })
                .filter(Objects::nonNull) // Filter out null users
                .collect(Collectors.toList());
    }
    @Override
    public Optional<Course> getCourseById(Long courseId) {
        // Fetch and return course by ID
        return courseRepository.findById(courseId) ;
    }

    public Long getUserIdForStudent(Long studentId){
        return studentRepository.getUserId(studentId);
    }
    public boolean removeStudentFromCourse(Long courseId, Long studentId) {
        // Check if the course exists
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isEmpty()) {
            throw new IllegalArgumentException("Course with ID " + courseId + " not found");
        }

        Course course = courseOptional.get();

        // Check if the student exists
        Optional<Student> studentOptional = studentRepository.findById(studentId);
        if (studentOptional.isEmpty()) {
            throw new IllegalArgumentException("Student with ID " + studentId + " not found");
        }

        Student student = studentOptional.get();

        // Remove the student from the course's student list
        if (!course.getStudents().contains(student)) {
            throw new IllegalStateException("Student with ID " + studentId + " is not enrolled in the course with ID " + courseId);
        }

        course.getStudents().remove(student);

        // Save the updated course
        courseRepository.save(course);

        return true; // Student successfully removed
    }

    public boolean deleteCourse(Long courseId){
        // Check if the course exists
        Optional<Course> courseOptional = courseRepository.getCourseByCourseId(courseId);
        if (courseOptional.isEmpty()) {
            throw new IllegalArgumentException("Course with ID " + courseId + " not found");
        }
        // Delete the course
        courseRepository.deleteCourseByCourseId(courseId);
        return true; // Course successfully deleted
    }
}
