package com.Java.LMS.platform.domain.Entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "instructors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Instructor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long InstructorId;

    // Foreign key (userId)
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false, unique = true)
    private User User;

    private String Department;
    private String Title;

    @Column(columnDefinition = "jsonb")
    private String AdditionalInfo;

    // OneToOne
//    @Column(name = "user_id", unique = true)
//    private Long UserId;

    public Long getInstructorId() {
        return InstructorId;
    }

    public void setInstructorId(Long instructorId) {
        InstructorId = instructorId;
    }

    public User getUser() {
        return User;
    }

    public void setUser(User user) {
        User = user;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getAdditionalInfo() {
        return AdditionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        AdditionalInfo = additionalInfo;
    }

//    public Long getUserId() {
//        return UserId;
//    }
//
//    public void setUserId(Long userId) {
//        UserId = userId;
//    }
}
