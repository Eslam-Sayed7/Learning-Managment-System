package com.Java.LMS.platform.infrastructure.repository;

import com.Java.LMS.platform.domain.Entities.AssessmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssessmentTypeRepository extends JpaRepository<AssessmentType, Long> {
     Optional<AssessmentType> findById(Long Id);
}
