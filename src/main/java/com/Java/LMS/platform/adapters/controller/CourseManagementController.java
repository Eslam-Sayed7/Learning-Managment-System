package com.Java.LMS.platform.adapters.controller;

import com.Java.LMS.platform.domain.Entities.*;
import com.Java.LMS.platform.infrastructure.repository.AdminRepository;
import com.Java.LMS.platform.service.CourseService;
import com.Java.LMS.platform.service.FileStorageService;
import com.Java.LMS.platform.service.LessonService;
import com.Java.LMS.platform.service.dto.CourseRequestModel;
import com.Java.LMS.platform.service.dto.EnrollmentRequestModel;
import com.Java.LMS.platform.service.dto.LessonRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
public class CourseManagementController {

    private final CourseService courseService;
    private final LessonService lessonService;
    private FileStorageService fileStorageService;
    private AdminRepository adminRepository;


    @Autowired
    public CourseManagementController(CourseService courseService, LessonService lessonService , FileStorageService fileStorageService , AdminRepository adminRepository) {
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.fileStorageService = fileStorageService;
        this.adminRepository = adminRepository;
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

//    @PostMapping("/{courseId}/lessons/create") // DONE
//    public ResponseEntity<?> createLesson(@PathVariable Long courseId, @RequestBody LessonRequestModel lessonRequest) {
//        Optional<Course> course = courseService.getCourseById(courseId);
//        if (course.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
//        }
//
//        boolean lessonExists = lessonService.isLessonFound(courseId , lessonRequest.getLessonName());
//
//        if (lessonExists) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lesson with the same title already exists for this course!");
//        }
//
//        Lesson lesson = lessonService.createLesson(courseId, lessonRequest);
//        return ResponseEntity.status(HttpStatus.CREATED).body(lesson);
//    }
@PostMapping("/{courseId}/lessons/create")
    public ResponseEntity<?> createLesson(
            @PathVariable Long courseId,
            @ModelAttribute LessonRequestModel lessonRequest // Use @ModelAttribute for Multipart data
    ) {
        lessonRequest.setCourseId(courseId);
        if (lessonRequest.getFile() == null || lessonRequest.getLessonName() == null || lessonRequest.getCourseId() == null ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File and lessonName and courseId upload is required for the lesson!");
        }
        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
        }
        boolean lessonExists = lessonService.isLessonFound(courseId, lessonRequest.getLessonName());
        if (lessonExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lesson with the same title already exists for this course!");
        }

        if (lessonRequest.getFile() == null || lessonRequest.getFile().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File upload is required for the lesson!");
        }
        // Check if the file is null or empty
        if (lessonRequest.getFile() == null || lessonRequest.getFile().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File upload is required for the lesson!");
        }

        // Validate file type
        String fileName = lessonRequest.getFile().getOriginalFilename();
        if (fileName == null || !(fileName.endsWith(".pdf") || fileName.endsWith(".mp4"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file type! Only PDF and MP4 files are allowed.");
        }
        String extension ;
        if (fileName.endsWith(".pdf")){
            extension = "PDF";
        }else{
            extension = "Video";
        }
        String fileUrl;
        try {
            // Upload file to your storage (e.g., AWS S3, Google Drive, or local storage)
            fileUrl = fileStorageService.saveFile(lessonRequest.getFile() , lessonRequest.getCourseId() , lessonRequest.getLessonName()); // Implement this in your service
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
        }
        if (fileUrl == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed ");
        }

        Lesson lesson = new Lesson();
        lesson.setCourseId(courseId);
        lesson.setLessonName(lessonRequest.getLessonName());
        lesson.setLessonDate(lessonRequest.getLessonDate() != null ? lessonRequest.getLessonDate() : LocalDateTime.now());
        lesson.setLessonType(extension);
        lesson.setFileUrl(fileUrl); // Save the uploaded file URL
        Lesson savedLesson = lessonService.createLesson(lesson); // Save the lesson to the database
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLesson);
    }


    // Enrollment Management
    @GetMapping     // DONE
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        for(Course course : courses){
            List<Student> students = course.getStudents();
            for(Student student : students){
                student.setCourses(null); // not accesses
            }
        }
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/enroll")
    public ResponseEntity<String> enrollStudent(@RequestBody EnrollmentRequestModel enrollmentRequest) {

        Optional<Course> course = courseService.getCourseById(enrollmentRequest.getCourseId());
        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
        }
        // Fetch the list of enrolled students as User objects
        List<User> enrolledUsers = courseService.getEnrolledStudents(enrollmentRequest.getCourseId());

        // Check if the student is already enrolled by matching the studentId
        boolean alreadyEnrolled = enrolledUsers.stream()
                .anyMatch(user -> user.getUserId().equals(courseService.getUserIdForStudent(enrollmentRequest.getStudentId())));

        if (alreadyEnrolled) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student is already enrolled in this course!");
        }

        // Enroll the student in the course
        courseService.enrollStudent(enrollmentRequest.getCourseId(), enrollmentRequest);
        return ResponseEntity.ok("Enrollment successful!");
    }

    @GetMapping("/{courseId}/students")
    public ResponseEntity<List<String>> getEnrolledStudents(@PathVariable Long courseId) {
        // Fetch the list of enrolled users
        List<User> enrolledUsers = courseService.getEnrolledStudents(courseId);

        // Map the User objects to their usernames
        List<String> usernames = enrolledUsers.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        return ResponseEntity.ok(usernames);
    }

    // Attendance Management
    @PostMapping("/{courseId}/lessons/{lessonId}/generate-otp")
    public ResponseEntity<String> generateOtp(@PathVariable Long courseId, @PathVariable Long lessonId, @RequestParam Long instructorId) {
        // Verify if the course exists
        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
        }

        // Check if the instructor is the owner of the course
        if (!course.get().getInstructorId().equals(instructorId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the owner instructor can generate an OTP for this course!");
        }

        // Verify if the lesson exists and belongs to the specified course
        Optional<Lesson> lesson = lessonService.getLessonById(lessonId);
        if (lesson.isEmpty() || !lesson.get().getCourseId().equals(courseId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found or does not belong to the specified course!");
        }

        // Check if an OTP has already been generated
        if (lesson.get().getOtp() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP has already been generated for this lesson!");
        }

        // Generate the OTP
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

        // Check if the course exists
        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
        }

        // Fetch the list of enrolled users and check if the student is enrolled
        boolean isEnrolled = courseService.getEnrolledStudents(courseId).stream()
                .anyMatch(user -> user.getUserId().equals(courseService.getUserIdForStudent(studentId)));

        if (!isEnrolled) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Student is not enrolled in this course!");
        }

        // Record attendance and validate the OTP
        boolean isVerified = lessonService.recordAttendance(lessonId, otp, studentId);
        if (isVerified) {
            return ResponseEntity.ok("Attendance recorded successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP!");
        }
    }

    @DeleteMapping("/{courseId}/students/{studentId}")
    public ResponseEntity<String> removeStudentFromCourse(
            @PathVariable Long courseId,
            @PathVariable Long studentId,
            @RequestParam Long instructorId) {

        // Check if the course exists
        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
        }

        // Check if the instructor is the owner of the course
        if (!course.get().getInstructorId().equals(instructorId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the instructor of this course!");
        }

        // Check if the student is enrolled in the course
        boolean isEnrolled = courseService.getEnrolledStudents(courseId).stream()
                .anyMatch(user -> user.getUserId().equals(courseService.getUserIdForStudent(studentId)));

        if (!isEnrolled) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Student is not enrolled in this course!");
        }

        // Remove the student from the course
        boolean removed = courseService.removeStudentFromCourse(courseId, studentId);
        if (removed) {
            return ResponseEntity.ok("Student removed from the course successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while removing the student from the course.");
        }
    }

    @DeleteMapping("/{courseId}/lessons/{lessonId}/delete")
    public ResponseEntity<String> deleteLesson(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            @RequestParam Long instructorId) {

        // Check if the course exists
        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
        }

        // Check if the instructor is the owner of the course
        if (!course.get().getInstructorId().equals(instructorId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not the instructor of this course!");
        }

        // Check if the lesson exists and belongs to the specified course
        Optional<Lesson> lesson = lessonService.getLessonById(lessonId);
        if (lesson.isEmpty() || !lesson.get().getCourseId().equals(courseId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found or does not belong to the specified course!");
        }

        // Delete the lesson
        boolean deleted = lessonService.deleteLesson(lessonId);
        if (deleted) {
            return ResponseEntity.ok("Lesson deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the lesson.");
        }
    }

    @DeleteMapping("/{courseId}/delete")
    public ResponseEntity<String> deleteCourse(
            @PathVariable Long courseId,
            @RequestParam Long adminId) {
        if (adminId == null || courseId == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("AdminId and CourseId is required!");
        }

        // Check if the course exists
        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
        }

        Optional<Admin> admin = adminRepository.findById(adminId);
        if (admin.isEmpty()) {
//            System.out.println(admin.get().getAdminId());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can delete a course!");
        }

        // Remove all students from the course
        List<User> enrolledStudents = courseService.getEnrolledStudents(courseId);
        for (User student : enrolledStudents) {
            courseService.removeStudentFromCourse(courseId, student.getUserId());
        }

        // Remove all lessons associated with the course
        List<Lesson> lessons = lessonService.getLessonsByCourseId(courseId);
        for (Lesson lesson : lessons) {
            lessonService.deleteLesson(lesson.getId());
        }

        // Delete the course
        boolean deleted = courseService.deleteCourse(courseId);
        if (deleted) {
            return ResponseEntity.ok("Course deleted successfully, along with all enrolled students , all lesson !");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while deleting the course.");
        }
    }

}
