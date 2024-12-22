package com.Java.LMS.platform.service.dto.Course;

import java.util.Date;
import java.util.Map;

public class SubmissionRequestDto {
    private Long studentId;
    private Long courseId;
    private Long AssessmentId;
    private Map<Long, Long> questionChoiceMap;
    private Date submissionDate = new Date();

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

    public Long getAssessmentId() {
        return AssessmentId;
    }

    public void setAssessmentId(Long assessmentId) {
        AssessmentId = assessmentId;
    }

    public Map<Long, Long> getQuestionChoiceMap() {
        return questionChoiceMap;
    }

    public void setQuestionChoiceMap(Map<Long, Long> questionChoiceMap) {
        this.questionChoiceMap = questionChoiceMap;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }
}
