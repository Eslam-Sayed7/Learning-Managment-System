package com.Java.LMS.platform.service.dto;

public class CourseRequestModel {


//CREATE TABLE courses (
//    course_id SERIAL PRIMARY KEY,
//    course_name VARCHAR(255) NOT NULL,
//    description TEXT,
//    instructor_id INT REFERENCES instructors(instructor_id) ON DELETE SET NULL
//);

    private String title;
    private String description;
    private Long getInstructor_id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Long getGetInstructor_id() {
        return getInstructor_id;
    }

    public void setGetInstructor_id(Long getInstructor_id) {
        this.getInstructor_id = getInstructor_id;
    }
}
