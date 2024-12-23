package com.Java.LMS.platform.service.dto;



public class ProgressTrackingDTO {
    private Long studentId;
    private Long courseId;
    private Long assignment_submission;
    private double attendancePercentage;  // Only include necessary fields

    public ProgressTrackingDTO(Long studentId, double attendancePercentage, Long assignment_submission, Long courseId) {
        this.studentId = studentId;
        this.attendancePercentage = attendancePercentage;
        this.assignment_submission=assignment_submission;
        this.courseId=courseId;
    }

    public ProgressTrackingDTO() {

    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }
    public Long getAssignmentSubmission() {
        return assignment_submission;
    }
    public void setAssignmentSubmission(Long assignment_submission) {
        this.assignment_submission=assignment_submission;
    }
    public Long getCourseId() {
        return courseId;
    }
    public void setCourseId(Long courseId) {
        this.courseId=courseId;
    }

}
