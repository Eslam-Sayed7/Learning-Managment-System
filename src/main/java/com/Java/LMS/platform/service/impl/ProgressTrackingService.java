package com.Java.LMS.platform.service.impl;

import com.Java.LMS.platform.domain.Entities.ProgressTracking;
import com.Java.LMS.platform.infrastructure.repository.ProgressTrackingRepository;
import com.Java.LMS.platform.service.dto.ProgressTrackingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProgressTrackingService {

    @Autowired
    private ProgressTrackingRepository progressTrackingRepository;

    public ProgressTrackingDTO getProgressByStudentId(Long studentId) {
        ProgressTracking progressTracking = progressTrackingRepository.findByStudent_StudentId(studentId);
        ProgressTrackingDTO progressTrackingDTO = new ProgressTrackingDTO();
        progressTrackingDTO.setStudentId(progressTracking.getStudent().getStudentId());
        progressTrackingDTO.setAttendancePercentage(progressTracking.getAttendanceCount());
        progressTrackingDTO.setCourseId(progressTracking.getCourse().getCourseId());
        progressTrackingDTO.setAssignmentSubmission(progressTracking.getAssignmentSubmitted());
        return progressTrackingDTO;
    }
    public ProgressTracking saveProgress(ProgressTracking progressTracking) {
        progressTracking.setLastUpdated(LocalDateTime.now());
        return progressTrackingRepository.save(progressTracking);
    }
}