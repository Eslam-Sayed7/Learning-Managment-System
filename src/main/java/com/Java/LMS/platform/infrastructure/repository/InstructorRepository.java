package com.Java.LMS.platform.infrastructure.repository;

import com.Java.LMS.platform.domain.Entities.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    @Override
    Optional<Instructor> findById(Long aLong);

}

