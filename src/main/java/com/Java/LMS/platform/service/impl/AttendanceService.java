package com.Java.LMS.platform.service.impl;

import com.Java.LMS.platform.domain.Entities.ProgressTracking;
import com.Java.LMS.platform.infrastructure.repository.AttendanceRepository;
import com.Java.LMS.platform.infrastructure.repository.LessonRepository;
import com.Java.LMS.platform.infrastructure.repository.ProgressTrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final LessonRepository lessonRepository;
    private final ProgressTrackingRepository progressRepository;
    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository, LessonRepository lessonRepository, ProgressTrackingRepository progressRepository) {
        this.attendanceRepository = attendanceRepository;
        this.lessonRepository = lessonRepository;
        this.progressRepository = progressRepository;
    }

    public double calculateAttendancePercentage(Long studentId) {
        // Get the total verified attendances for the student
        long totalAttendances = attendanceRepository.countVerifiedAttendancesByStudent(studentId);

        // Get the total number of lessons for the student
        long totalLessons = lessonRepository.countTotalLessonsForStudent(studentId);

        // Avoid division by zero
        if (totalLessons == 0) {
            return 0.0;
        }

        // Calculate and return the percentage
        return ((double) totalAttendances / totalLessons) * 100;
    }
    public void updateAttendancePercentageInProgress(Long studentId) {
        // Calculate the attendance percentage
        double percentage = calculateAttendancePercentage(studentId);

        // Fetch the progress record for the student (assuming it exists)
        ProgressTracking progress = progressRepository.findByStudent_StudentId(studentId);

        // If progress record exists, update the attendance count column
        if (progress != null) {
            progress.setAttendanceCount(percentage);
            progressRepository.save(progress); // Save the updated progress
        }
    }
}
