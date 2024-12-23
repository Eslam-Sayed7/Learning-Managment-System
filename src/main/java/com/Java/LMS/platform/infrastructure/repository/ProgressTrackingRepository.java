package com.Java.LMS.platform.infrastructure.repository;

import com.Java.LMS.platform.domain.Entities.ProgressTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProgressTrackingRepository extends JpaRepository<ProgressTracking, Long> {

    ProgressTracking findByStudent_StudentId(Long studentId);
}
