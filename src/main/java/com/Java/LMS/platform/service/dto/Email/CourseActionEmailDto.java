package com.Java.LMS.platform.service.dto.Email;

public class CourseActionEmailDto extends EmailFormateDto {
    private long courseId;

    public long getCourseId() { return courseId; }
    public void setCourseId(long courseId) { this.courseId = courseId; }
}
