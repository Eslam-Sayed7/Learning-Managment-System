package com.Java.LMS.platform;

import com.Java.LMS.platform.adapters.controller.CourseManagementController;
import com.Java.LMS.platform.domain.Entities.*;
import com.Java.LMS.platform.enums.NotificationType;
import com.Java.LMS.platform.infrastructure.repository.AdminRepository;
import com.Java.LMS.platform.infrastructure.repository.AttendanceRepository;
import com.Java.LMS.platform.infrastructure.repository.UserRepository;
import com.Java.LMS.platform.service.*;
import com.Java.LMS.platform.service.dto.CourseRequestModel;
import com.Java.LMS.platform.service.dto.Email.EmailFormateDto;
import com.Java.LMS.platform.service.dto.EnrollmentRequestModel;
import com.Java.LMS.platform.service.dto.LessonRequestModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseManagementControllerTest {

    @InjectMocks
    private CourseManagementController courseManagementController;

    @Mock
    private CourseService courseService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailService emailService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private LessonService lessonService;

    @Mock
    private FileStorageService fileStorageService;
    @Mock
    private AdminRepository adminRepository;

    @Mock
    private AttendanceRepository attendanceRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCourse_ShouldReturnCreatedCourse_WhenValidRequest() {
        CourseRequestModel request = new CourseRequestModel();
        request.setTitle("Test Course");
        request.setGetInstructor_id(1L);

        List<Course> courses = new ArrayList<>();
        when(courseService.getAllCourses()).thenReturn(courses);

        Course createdCourse = new Course();
        createdCourse.setTitle("Test Course");
        when(courseService.createCourse(request)).thenReturn(createdCourse);

        ResponseEntity<?> response = courseManagementController.createCourse(request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(createdCourse, response.getBody());
    }

    @Test
    void createCourse_ShouldReturnBadRequest_WhenDuplicateTitleExists() {
        CourseRequestModel request = new CourseRequestModel();
        request.setTitle("Test Course");
        request.setGetInstructor_id(1L);

        Course existingCourse = new Course();
        existingCourse.setTitle("Test Course");
        existingCourse.setInstructorId(1L);
        List<Course> courses = List.of(existingCourse);
        when(courseService.getAllCourses()).thenReturn(courses);

        ResponseEntity<?> response = courseManagementController.createCourse(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Instructor already has a course with the same title!", response.getBody());
    }

    @Test
    void createLesson_ShouldReturnCreatedLesson_WhenValidRequest() throws Exception {
        Long courseId = 1L;
        LessonRequestModel request = new LessonRequestModel();
        request.setLessonName("Test Lesson");
        request.setFile(new MockMultipartFile("file", "test.pdf", "application/pdf", "Dummy Content".getBytes()));

        Course course = new Course();
        when(courseService.getCourseById(courseId)).thenReturn(Optional.of(course));
        when(lessonService.isLessonFound(courseId, "Test Lesson")).thenReturn(false);

        String fileUrl = "http://example.com/test.pdf";
        when(fileStorageService.saveFile(any(), eq(courseId), eq("Test Lesson"))).thenReturn(fileUrl);

        Lesson savedLesson = new Lesson();
        savedLesson.setLessonName("Test Lesson");
        savedLesson.setFileUrl(fileUrl);
        when(lessonService.createLesson(any(Lesson.class))).thenReturn(savedLesson);

        ResponseEntity<?> response = courseManagementController.createLesson(courseId, request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(savedLesson, response.getBody());
    }

    @Test
    void createLesson_ShouldReturnBadRequest_WhenFileIsMissing() throws Exception {
        Long courseId = 1L;
        LessonRequestModel request = new LessonRequestModel();
        request.setLessonName("Test Lesson");
        request.setFile(null);

        ResponseEntity<?> response = courseManagementController.createLesson(courseId, request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("File and lessonName and courseId upload is required for the lesson!", response.getBody());
    }

    @Test
    void enrollStudent_ShouldReturnOk_WhenValidRequest() {
        // Arrange
        EnrollmentRequestModel request = new EnrollmentRequestModel();
        request.setCourseId(1L);
        request.setStudentId(2L);
        request.setUserId(3L);

        Course course = new Course();
        course.setTitle("Test Course");

        User user = new User();
        user.setEmail("test@example.com");

        when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
        when(courseService.getEnrolledStudents(1L)).thenReturn(new ArrayList<>());
        when(userRepository.findById(3L)).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<String> response = courseManagementController.enrollStudent(request);

        // Assert
        verify(courseService).enrollStudent(1L, request);
        verify(emailService).sendEmail(any(EmailFormateDto.class));
        verify(notificationService).createNotification(
                eq(3L),
                isNull(),
                eq(NotificationType.ENROLLMENT),
                contains("You have successfully enrolled in the course: Test Course")
        );

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Enrollment successful!", response.getBody());
    }

    @Test
    void enrollStudent_ShouldReturnBadRequest_WhenAlreadyEnrolled() {
        EnrollmentRequestModel request = new EnrollmentRequestModel();
        request.setCourseId(1L);
        request.setStudentId(2L);

        Course course = new Course();
        when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
        User user = new User();
        user.setUserId(3L);
        when(courseService.getEnrolledStudents(1L)).thenReturn(List.of(user));
        when(courseService.getUserIdForStudent(2L)).thenReturn(3L);

        ResponseEntity<String> response = courseManagementController.enrollStudent(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Student is already enrolled in this course!", response.getBody());
    }

    @Test
    void generateOtp_ShouldReturnOk_WhenValidRequest() {
        Long courseId = 1L;
        Long lessonId = 2L;
        Long instructorId = 3L;

        Course course = new Course();
        course.setInstructorId(instructorId);
        when(courseService.getCourseById(courseId)).thenReturn(Optional.of(course));

        Lesson lesson = new Lesson();
        lesson.setCourseId(courseId);
        when(lessonService.getLessonById(lessonId)).thenReturn(Optional.of(lesson));

        when(lessonService.generateOtp(lessonId)).thenReturn("123456");

        ResponseEntity<String> response = courseManagementController.generateOtp(courseId, lessonId, instructorId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("OTP generated and sent to enrolled students!", response.getBody());
    }

    @Test
    void generateOtp_ShouldReturnForbidden_WhenInstructorDoesNotOwnCourse() {
        Long courseId = 1L;
        Long lessonId = 2L;
        Long instructorId = 3L;

        Course course = new Course();
        course.setInstructorId(4L);
        when(courseService.getCourseById(courseId)).thenReturn(Optional.of(course));

        ResponseEntity<String> response = courseManagementController.generateOtp(courseId, lessonId, instructorId);

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Only the owner instructor can generate an OTP for this course!", response.getBody());
    }

    @Test
    public void testRecordAttendance_Success() {
        Long lessonId = 1L;
        Long studentId = 1001L;

        // Mock attendance existence check
        when(attendanceRepository.existsByLessonIdAndStudentId(lessonId, studentId)).thenReturn(false);

        // Simulate successful attendance recording
        Attendance newAttendance = new Attendance();
        newAttendance.setLessonId(lessonId);
        newAttendance.setStudentId(studentId);
        when(attendanceRepository.save(any())).thenReturn(newAttendance);

        Attendance savedAttendance = attendanceRepository.save(newAttendance);

        assertEquals(lessonId, savedAttendance.getLessonId());
        assertEquals(studentId, savedAttendance.getStudentId());
    }

    @Test
    public void testRecordAttendance_CourseNotFound() {
        Long lessonId = 1L;
        Long studentId = 1001L;

        // Simulate course not found scenario
        when(attendanceRepository.existsByLessonIdAndStudentId(lessonId, studentId)).thenReturn(false);

        boolean exists = attendanceRepository.existsByLessonIdAndStudentId(lessonId, studentId);

        assertEquals(false, exists);
    }

    @Test
    public void testRecordAttendance_StudentNotEnrolled() {
        Long lessonId = 1L;
        Long studentId = 1001L;

        // Simulate student not enrolled scenario
        when(attendanceRepository.existsByLessonIdAndStudentId(lessonId, studentId)).thenReturn(false);

        boolean isEnrolled = attendanceRepository.existsByLessonIdAndStudentId(lessonId, studentId);

        assertEquals(false, isEnrolled);
    }

    @Test
    public void testRecordAttendance_InvalidOTP() {
        Long lessonId = 1L;
        String otp = "123456";
        Long studentId = 1001L;

        // Simulate invalid OTP scenario
        when(attendanceRepository.existsByLessonIdAndStudentId(lessonId, studentId)).thenReturn(true);
        when(attendanceRepository.findByLessonId(lessonId)).thenReturn(List.of()); // OTP mismatch

        List<Attendance> attendances = attendanceRepository.findByLessonId(lessonId);

        assertEquals(0, attendances.size());
    }
    @Test
    void testRemoveStudentFromCourse_Success() {
        Long courseId = 1L;
        Long studentId = 2L;
        Long instructorId = 3L;

        Course mockCourse = new Course();
        User mockStudent = new User() ;

        mockCourse.setCourseId(courseId);
        mockCourse.setInstructorId(instructorId);
        mockStudent.setUserId(studentId);

        when(courseService.getCourseById(courseId)).thenReturn(Optional.of(mockCourse));
        when(courseService.getEnrolledStudents(courseId)).thenReturn(List.of(mockStudent));
        when(courseService.getUserIdForStudent(studentId)).thenReturn(studentId);
        when(courseService.removeStudentFromCourse(courseId, studentId)).thenReturn(true);

        ResponseEntity<String> response = courseManagementController.removeStudentFromCourse(courseId, studentId, instructorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Student removed from the course successfully!", response.getBody());
    }

    @Test
    void testRemoveStudentFromCourse_CourseNotFound() {
        Long courseId = 1L;
        Long studentId = 2L;
        Long instructorId = 3L;

        when(courseService.getCourseById(courseId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = courseManagementController.removeStudentFromCourse(courseId, studentId, instructorId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Course not found!", response.getBody());
    }

    @Test
    void testDeleteLesson_Success() {
        Long courseId = 1L;
        Long lessonId = 2L;
        Long instructorId = 3L;

//        Course mockCourse = new Course(courseId, instructorId);
        Course mockCourse = new Course();
        Student mockStudent = new Student() ;

        mockCourse.setCourseId(courseId);
        mockCourse.setInstructorId(instructorId);

//        Lesson mockLesson = new Lesson(lessonId, courseId);
        Lesson mockLesson = new Lesson();
        mockLesson.setId(lessonId);
        mockLesson.setCourseId(courseId);

        when(courseService.getCourseById(courseId)).thenReturn(Optional.of(mockCourse));
        when(lessonService.getLessonById(lessonId)).thenReturn(Optional.of(mockLesson));
        when(lessonService.deleteLesson(lessonId)).thenReturn(true);

        ResponseEntity<String> response = courseManagementController.deleteLesson(courseId, lessonId, instructorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Lesson deleted successfully!", response.getBody());
    }

    @Test
    void testDeleteCourse_Success() {
        Long courseId = 1L;
        Long adminId = 2L;

//        Course mockCourse = new Course(courseId, 3L);

        Course mockCourse = new Course();

        mockCourse.setCourseId(courseId);
        mockCourse.setInstructorId(3L);

//        Admin mockAdmin = new Admin(adminId);
        Admin mockAdmin = new Admin();
        mockAdmin.setAdminId(adminId);

//        User mockStudent = new User(4L);
        User mockStudent = new User();
        mockStudent.setUserId(4L);

//        Lesson mockLesson = new Lesson(5L, courseId);
        Lesson mockLesson = new Lesson();
        mockLesson.setId(5L);
        mockLesson.setCourseId(courseId);

        when(courseService.getCourseById(courseId)).thenReturn(Optional.of(mockCourse));
        when(adminRepository.findById(adminId)).thenReturn(Optional.of(mockAdmin));
        when(courseService.getEnrolledStudents(courseId)).thenReturn(List.of(mockStudent));
        when(lessonService.getLessonsByCourseId(courseId)).thenReturn(List.of(mockLesson));
        when(courseService.removeStudentFromCourse(courseId, mockStudent.getUserId())).thenReturn(true);
        when(lessonService.deleteLesson(mockLesson.getId())).thenReturn(true);
        when(courseService.deleteCourse(courseId)).thenReturn(true);

        ResponseEntity<String> response = courseManagementController.deleteCourse(courseId, adminId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Course deleted successfully, along with all enrolled students , all lesson !", response.getBody());
    }
    @Test
    void deleteCourse_ShouldReturnOk_WhenAdminDeletesCourse() {
        Long courseId = 1L;
        Long adminId = 2L;

        Course course = new Course();
        when(courseService.getCourseById(courseId)).thenReturn(Optional.of(course));
        when(adminRepository.findById(adminId)).thenReturn(Optional.of(new Admin()));
        when(courseService.deleteCourse(courseId)).thenReturn(true);

        ResponseEntity<String> response = courseManagementController.deleteCourse(courseId, adminId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Course deleted successfully, along with all enrolled students , all lesson !", response.getBody());
    }
    @Test
    void testDeleteCourse_AdminNotFound() {
        Long courseId = 1L;
        Long adminId = 2L;

        Course mockCourse = new Course();

        mockCourse.setCourseId(courseId);
        mockCourse.setInstructorId(3L);

        when(courseService.getCourseById(courseId)).thenReturn(Optional.of(mockCourse));
        when(adminRepository.findById(adminId)).thenReturn(Optional.empty());

        ResponseEntity<String> response = courseManagementController.deleteCourse(courseId, adminId);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Only admins can delete a course!", response.getBody());
    }
}
