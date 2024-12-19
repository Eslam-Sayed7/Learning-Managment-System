package com.Java.LMS.platform.service.dto;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

public class LessonRequestModel {
    private String lessonName;
    private Long courseId;
    private LocalDateTime lessonDate;
    private String lessonType; // "PDF" or "Video"
    private MultipartFile file; // Multipart file for PDF or Video

    // Getters and Setters
    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public LocalDateTime getLessonDate() {
        return lessonDate;
    }

    public void setLessonDate(LocalDateTime lessonDate) {
        this.lessonDate = lessonDate;
    }

    public String getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
