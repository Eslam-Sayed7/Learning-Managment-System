package com.Java.LMS.platform.adapters.controller;

import com.Java.LMS.platform.domain.Entities.Course;
import com.Java.LMS.platform.domain.Entities.Lesson;
import com.Java.LMS.platform.service.CourseService;
import com.Java.LMS.platform.service.LessonService;
import com.Java.LMS.platform.service.dto.CourseRequestModel;
import com.Java.LMS.platform.service.dto.EnrollmentRequestModel;
import com.Java.LMS.platform.service.dto.LessonRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/courses")
public class CourseManagementController {

    private final CourseService courseService;
    private final LessonService lessonService;

    @Autowired
    public CourseManagementController(CourseService courseService, LessonService lessonService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
    }

    // Course Creation
    @PostMapping("/create")         // DONE
    public ResponseEntity<?> createCourse(@RequestBody CourseRequestModel courseRequest) {
        System.out.println(courseRequest.getTitle());
        List<Course> existingCourses = courseService.getAllCourses();
        boolean courseExists = existingCourses.stream()
                .anyMatch(course -> course.getTitle().equalsIgnoreCase(courseRequest.getTitle())
                        && (course.getInstructorId() == courseRequest.getGetInstructor_id()));

        if (courseExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Instructor already has a course with the same title!");
        }

        Course course = courseService.createCourse(courseRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(course);
    }

    @PostMapping("/{courseId}/lessons/create")
    public ResponseEntity<?> createLesson(@PathVariable Long courseId, @RequestBody LessonRequestModel lessonRequest) {
        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
        }

//        boolean lessonExists = course.get().getLessons().stream()
//                .anyMatch(lesson -> lesson.getLessonName().equalsIgnoreCase(lessonRequest.getLessonName()));
//
//        if (lessonExists) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lesson with the same title already exists for this course!");
//        }

        Lesson lesson = lessonService.createLesson(courseId, lessonRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(lesson);
    }

    // Enrollment Management
    @GetMapping     // DONE
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/enroll") // DONE
    public ResponseEntity<String> enrollStudent(@RequestBody EnrollmentRequestModel enrollmentRequest) {
        boolean alreadyEnrolled = courseService.getEnrolledStudents(enrollmentRequest.getCourseId()).contains(enrollmentRequest.getStudentId());
        if (alreadyEnrolled) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student is already enrolled in this course!");
        }

        courseService.enrollStudent(enrollmentRequest.getCourseId(), enrollmentRequest);
        return ResponseEntity.ok("Enrollment successful!");
    }

    @GetMapping("/{courseId}/students") // ~DONE
    public ResponseEntity<List<String>> getEnrolledStudents(@PathVariable Long courseId) {
        List<String> students = courseService.getEnrolledStudents(courseId);
        return ResponseEntity.ok(students);
    }

    // Attendance Management
    @PostMapping("/{courseId}/lessons/{lessonId}/generate-otp")
    public ResponseEntity<String> generateOtp(@PathVariable Long courseId, @PathVariable Long lessonId) {
        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
        }

        Optional<Lesson> lesson = lessonService.getLessonById(lessonId);
        if (lesson.isEmpty() || !lesson.get().getCourse_id().equals(courseId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found or does not belong to the specified course!");
        }

        if (lesson.get().getOtp() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP has already been generated for this lesson!");
        }

        String otp = lessonService.generateOtp(lessonId);

        // Uncomment the following line after implementing notification logic
        // lessonService.notifyStudents(courseService.getEnrolledStudents(courseId), otp);

        return ResponseEntity.ok("OTP generated and sent to enrolled students!");
    }

    @PostMapping("/{courseId}/lessons/{lessonId}/attendance")
    public ResponseEntity<String> recordAttendance(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @RequestParam String otp,
            @RequestParam Long studentId) {

        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
        }

        boolean isEnrolled = courseService.getEnrolledStudents(courseId).contains(studentId);
        if (!isEnrolled) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Student is not enrolled in this course!");
        }

        boolean isVerified = lessonService.recordAttendance(lessonId, otp, studentId);
        if (isVerified) {
            return ResponseEntity.ok("Attendance recorded successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP!");
        }
    }

}
