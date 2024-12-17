package com.Java.LMS.platform.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Java.LMS.platform.domain.Entities.Student;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Override
    Optional<Student> findById(Long aLong);
}
