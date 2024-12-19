package com.Java.LMS.platform.service.impl;

import com.Java.LMS.platform.domain.Entities.Lesson;
import com.Java.LMS.platform.domain.Entities.Attendance;
import com.Java.LMS.platform.infrastructure.repository.LessonRepository;
import com.Java.LMS.platform.infrastructure.repository.AttendanceRepository;
import com.Java.LMS.platform.service.LessonService;
import com.Java.LMS.platform.service.dto.LessonRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
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
    public Lesson createLesson(Lesson lesson) {
        if (lesson.getLessonName() == null || lesson.getLessonName().isEmpty()) {
            throw new IllegalArgumentException("Lesson name is required.");
        }
        if (lesson.getCourseId() == null) {
            throw new IllegalArgumentException("Course ID is required.");
        }
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
    @Override
    public boolean deleteLesson(Long lessonId) {
        // Fetch the lesson by ID
        Optional<Lesson> lesson = lessonRepository.findById(lessonId);

        if (lesson.isPresent()) {
            // Delete the lesson
            LessonService.deleteFile(lesson.get().getFileUrl());
            lessonRepository.delete(lesson.get());
            return true;
        } else {
            // Lesson not found
            throw new IllegalArgumentException("Lesson with ID " + lessonId + " not found.");
        }
    }

    @Override
    public List<Lesson> getLessonsByCourseId(Long courseId) {
        if (courseId == null) {
            throw new IllegalArgumentException("Course ID is required.");
        }

        // Fetch lessons by course ID
        return lessonRepository.findLessonsByCourseId(courseId);
    }

}