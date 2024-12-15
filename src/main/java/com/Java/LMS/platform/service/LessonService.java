package com.Java.LMS.platform.service;

import com.Java.LMS.platform.domain.Entities.Lesson;
import com.Java.LMS.platform.service.dto.LessonRequestModel;

import java.util.Optional;

public interface LessonService {
    Lesson createLesson(Long courseId, LessonRequestModel request);
    String generateOtp(Long lessonId);
    boolean recordAttendance(Long lessonId, String otp, Long studentId);

    Optional<Lesson> getLessonById(Long lessonId);
}