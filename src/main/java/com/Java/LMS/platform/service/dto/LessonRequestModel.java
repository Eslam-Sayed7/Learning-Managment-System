package com.Java.LMS.platform.service.dto;

import java.time.LocalDateTime;


//    lesson_id SERIAL PRIMARY KEY,
//    course_id INT REFERENCES courses(course_id) ON DELETE CASCADE,
//    lesson_name VARCHAR(255) NOT NULL,
//    lesson_date TIMESTAMP NOT NULL,
//    otp VARCHAR(6),
//    otp_expiration TIMESTAMP,
//    is_active BOOLEAN DEFAULT TRUE

public class LessonRequestModel {
    private String lessonName;
    private Long courseId;
    private LocalDateTime lessonDate;


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
}
