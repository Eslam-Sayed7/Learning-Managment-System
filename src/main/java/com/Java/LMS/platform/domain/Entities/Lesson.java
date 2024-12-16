package com.Java.LMS.platform.domain.Entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

//CREATE TABLE lessons (
//    lesson_id SERIAL PRIMARY KEY,
//    course_id INT REFERENCES courses(course_id) ON DELETE CASCADE,
//    lesson_name VARCHAR(255) NOT NULL,
//    lesson_date TIMESTAMP NOT NULL,
//    otp VARCHAR(6),
//    otp_expiration TIMESTAMP,
//    is_active BOOLEAN DEFAULT TRUE
//);

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Long id;

    @Column(name = "lesson_name", nullable = false)
    private String lessonName;

    @Column(name = "lesson_date", nullable = false)
    private LocalDateTime lessonDate;

    @Column(name = "otp", length = 6)
    private String otp;

    @Column(name = "otp_expiration")
    private LocalDateTime otpExpiration;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "course_id", nullable = false)
    private Long course_id;
//    @ManyToOne
//    @JoinColumn(name = "course_id")
//    private Course course;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public LocalDateTime getLessonDate() {
        return lessonDate;
    }

    public void setLessonDate(LocalDateTime lessonDate) {
        this.lessonDate = lessonDate;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getOtpExpiration() {
        return otpExpiration;
    }

    public void setOtpExpiration(LocalDateTime otpExpiration) {
        this.otpExpiration = otpExpiration;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Long getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Long course_id) {
        this.course_id = course_id;
    }

}
