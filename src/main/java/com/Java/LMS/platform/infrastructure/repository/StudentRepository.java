package com.Java.LMS.platform.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Java.LMS.platform.domain.Entities.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Override
    Optional<Student> findById(Long id);

    @Query("SELECT s.userId FROM Student s WHERE s.studentId = :studentId")
    Long getUserId(@Param("studentId") Long studentId);

}
