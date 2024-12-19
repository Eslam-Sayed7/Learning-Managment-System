package com.Java.LMS.platform.service;

import com.Java.LMS.platform.domain.Entities.Lesson;
import com.Java.LMS.platform.service.dto.LessonRequestModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface LessonService {
    Lesson createLesson(Lesson lesson);
    String generateOtp(Long lessonId);
    boolean recordAttendance(Long lessonId, String otp, Long studentId);

    Optional<Lesson> getLessonById(Long lessonId);
    boolean isLessonFound(Long courseId, String lessonName);

    boolean deleteLesson(Long lessonId);

    List<Lesson> getLessonsByCourseId(Long courseId);

    public static boolean deleteFile(String filePath) {
        // Create a File object
        File file = new File(filePath);

        // Check if the file exists
        if (file.exists()) {
            // Attempt to delete the file
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("File deleted successfully. " + filePath);
                return true;
            } else {
                System.out.println("Failed to delete the file.");
                return false;
            }
        } else {
            System.out.println("File not found.");
            return false;
        }
    }

}
