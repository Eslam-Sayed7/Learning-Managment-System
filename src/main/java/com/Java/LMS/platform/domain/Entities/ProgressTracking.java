package com.Java.LMS.platform.domain.Entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "progress_tracking")
public class ProgressTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long progress_id;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id") // Maps to "student_id" in DB
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id") // Maps to "course_id" in DB
    private Course course;

    @Column(name = "assignments_submitted")
    private Long assignment_submitted;

    @Column(name = "attendance_count")
    private Double attendance_count;

    @Column(name = "last_updated")
    private LocalDateTime last_updated;

    // Getters and Setters

    public Long getProgressId() {
        return progress_id;
    }

    public void setProgressId(Long progress_id) {
        this.progress_id = progress_id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Long getAssignmentSubmitted() {
        return assignment_submitted;
    }

    public void setAssignmentSubmitted(Long assignment_submitted) {
        this.assignment_submitted = assignment_submitted;
    }

    public Double getAttendanceCount() {
        return attendance_count;
    }

    public void setAttendanceCount(Double attendance_count) {
        this.attendance_count = attendance_count;
    }

    public LocalDateTime getLastUpdated() {
        return last_updated;
    }

    public void setLastUpdated(LocalDateTime last_updated) {
        this.last_updated = last_updated;
    }
}
