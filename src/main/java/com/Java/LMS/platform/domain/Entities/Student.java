package com.Java.LMS.platform.domain.Entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long StudentId;

    // Foreign key (userId)
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false, unique = true)
    private User User;

    private String Major;
    private Integer YearOfStudy;

    @Column(name = "additional_info", columnDefinition = "jsonb")
    private String AdditionalInfo;

    // OneToOne
//    @Column(name = "user_id", unique = true)
//    private Long UserId;

    public Long getStudentId() {
        return StudentId;
    }

    public void setStudentId(Long studentId) {
        StudentId = studentId;
    }

    public String getMajor() {
        return Major;
    }

    public void setMajor(String major) {
        Major = major;
    }

    public Integer getYearOfStudy() {
        return YearOfStudy;
    }

    public void setYearOfStudy(Integer yearOfStudy) {
        YearOfStudy = yearOfStudy;
    }

    public String getAdditionalInfo() {
        return AdditionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        AdditionalInfo = additionalInfo;
    }

    public User getUser() {
        return User;
    }

    public void setUser(User user) {
        User = user;
    }

//    public Long getUserId() {
//        return UserId;
//    }
//
//    public void setUserId(Long userId) {
//        UserId = userId;
//    }
}