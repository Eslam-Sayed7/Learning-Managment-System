package com.Java.LMS.platform.service.dto;



//    enrollment_id SERIAL PRIMARY KEY,
//    student_id INT REFERENCES students(student_id) ON DELETE CASCADE,
//    course_id INT REFERENCES courses(course_id) ON DELETE CASCADE,
//    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
//    UNIQUE(student_id, course_id)

import java.time.LocalDateTime;

public class EnrollmentRequestModel {
    private Long studentId;
    private Long courseId;
    private LocalDateTime enrollment_date;

    private Long userId;

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public LocalDateTime getEnrollment_date() {
        return enrollment_date;
    }

    public void setEnrollment_date(LocalDateTime enrollment_date) {
        this.enrollment_date = enrollment_date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
