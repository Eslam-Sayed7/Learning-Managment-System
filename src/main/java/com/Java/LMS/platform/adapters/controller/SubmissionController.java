package com.Java.LMS.platform.adapters.controller;

import com.Java.LMS.platform.domain.Entities.Submission;
import com.Java.LMS.platform.infrastructure.repository.AssessmentRepository;
import com.Java.LMS.platform.infrastructure.repository.StudentRepository;
import com.Java.LMS.platform.service.dto.Course.SubmissionRequestDto;
import com.Java.LMS.platform.infrastructure.repository.SubmissionRepository;
import com.Java.LMS.platform.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/submissions")
public class SubmissionController {

    private GradeService gradeService;
    private StudentRepository studentRepository;
    private SubmissionRepository submissionRepository;
    private AssessmentRepository assessmentRepository;

    @Autowired
    SubmissionController(GradeService gradeService, StudentRepository studentRepository,
                         SubmissionRepository submissionRepository, AssessmentRepository assessmentRepository) {
        this.gradeService = gradeService;
        this.studentRepository = studentRepository;
        this.submissionRepository = submissionRepository;
        this.assessmentRepository = assessmentRepository;
    }
    @Transactional
    @PostMapping("create")
    public ResponseEntity<String> createSubmission(@RequestBody SubmissionRequestDto submissionDto) {
        Submission submission = new Submission();
        submission.setStudent(studentRepository.findById(submissionDto.getStudentId()).get());
        submission.setAssessment(assessmentRepository.findById(submissionDto.getAssessmentId()).get());
        try {
            submissionRepository.save(new Submission());
            return ResponseEntity.ok("Submission created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/{submissionId}/grade")
    public ResponseEntity<String> calculateGrade(@PathVariable int submissionId) {
        try {
            boolean result = gradeService.calculateGrade(submissionId);

            if (result) {
                return ResponseEntity.ok("Grade calculated successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to calculate grade");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}