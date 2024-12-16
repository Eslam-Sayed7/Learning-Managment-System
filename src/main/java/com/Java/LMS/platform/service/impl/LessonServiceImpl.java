package com.Java.LMS.platform.service.impl;

import com.Java.LMS.platform.domain.Entities.Lesson;
import com.Java.LMS.platform.domain.Entities.Attendance;
import com.Java.LMS.platform.infrastructure.repository.LessonRepository;
import com.Java.LMS.platform.infrastructure.repository.AttendanceRepository;
import com.Java.LMS.platform.service.LessonService;
import com.Java.LMS.platform.service.dto.LessonRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final AttendanceRepository attendanceRepository;

    @Autowired
    public LessonServiceImpl(LessonRepository lessonRepository, AttendanceRepository attendanceRepository) {
        this.lessonRepository = lessonRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public Lesson createLesson(Long courseId, LessonRequestModel request) {
        if (request.getLessonName() == null || request.getLessonName().isEmpty()) {
            throw new IllegalArgumentException("Lesson name is required.");
        }
        if (courseId == null) {
            throw new IllegalArgumentException("Course ID is required.");
        }

        // Create and save lesson
        Lesson lesson = new Lesson();
        lesson.setLessonName(request.getLessonName());
        lesson.setCourse_id(courseId);
        lesson.setLessonDate(request.getLessonDate() != null ? request.getLessonDate() : LocalDateTime.now());
        lesson.setActive(true);

        return lessonRepository.save(lesson);
    }

    @Override
    public String generateOtp(Long lessonId) {
        // Fetch lesson and check if it exists
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found."));

        // Generate a 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiration = LocalDateTime.now().plus(10, ChronoUnit.MINUTES); // OTP valid for 10 minutes

        // Update lesson with OTP and expiration time
        lesson.setOtp(otp);
        lesson.setOtpExpiration(expiration);
        lessonRepository.save(lesson);

        return otp;
    }

    @Override
    public boolean recordAttendance(Long lessonId, String otp, Long studentId) {
        // Fetch lesson and validate OTP
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found."));

        if (!lesson.isActive()) {
            throw new IllegalStateException("Lesson is not active.");
        }
        if (lesson.getOtp() == null || !lesson.getOtp().equals(otp)) {
            return false; // Invalid OTP
        }
        if (lesson.getOtpExpiration() == null || lesson.getOtpExpiration().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("OTP has expired.");
        }

        // Record attendance
        Attendance attendance = new Attendance();
        attendance.setLessonId(lessonId);
        attendance.setStudentId(studentId);
        attendance.setOtpEntered(otp);
        attendance.setIsVerified(true);
        attendance.setAttendanceDate(LocalDateTime.now());
        attendanceRepository.save(attendance);

        return true; // Attendance recorded successfully
    }

    @Override
    public Optional<Lesson> getLessonById(Long lessonId) {
        // Fetch lesson by ID
        return lessonRepository.findById(lessonId);
    }
    @Override
    public boolean isLessonFound(Long courseId, String lessonName) {
        if (courseId == null || lessonName == null || lessonName.isEmpty()) {
            throw new IllegalArgumentException("Course ID and Lesson Name are required.");
        }

        // Use repository to check if lesson exists
        return lessonRepository.findLessonByCourseIdAndName(courseId, lessonName).isPresent();
    }

}