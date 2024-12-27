package com.Java.LMS.platform.adapters.controller;

import com.Java.LMS.platform.domain.Entities.Assessment;
import com.Java.LMS.platform.domain.Entities.Student;
import com.Java.LMS.platform.domain.Entities.Submission;
import com.Java.LMS.platform.domain.Entities.User;
import com.Java.LMS.platform.infrastructure.repository.AssessmentRepository;
import com.Java.LMS.platform.infrastructure.repository.StudentRepository;
import com.Java.LMS.platform.infrastructure.repository.UserRepository;
import com.Java.LMS.platform.service.EmailService;
import com.Java.LMS.platform.service.dto.Course.SubmissionRequestDto;
import com.Java.LMS.platform.infrastructure.repository.SubmissionRepository;
import com.Java.LMS.platform.service.GradeService;
import com.Java.LMS.platform.service.dto.Email.EmailFormateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/submissions")
public class SubmissionController {

    private GradeService gradeService;
    private StudentRepository studentRepository;
    private SubmissionRepository submissionRepository;
    private AssessmentRepository assessmentRepository;
    private UserRepository userRepository;
    private EmailService emailService;

    @Autowired
    public SubmissionController(GradeService gradeService, StudentRepository studentRepository,
                                SubmissionRepository submissionRepository, AssessmentRepository assessmentRepository,
                                UserRepository userRepository, EmailService emailService) {
        this.gradeService = gradeService;
        this.studentRepository = studentRepository;
        this.submissionRepository = submissionRepository;
        this.assessmentRepository = assessmentRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public SubmissionController(SubmissionRepository submissionRepository, AssessmentRepository assessmentRepository, StudentRepository studentRepository, GradeService gradeService) {
    }

    @Transactional
    @PostMapping("create")
    public ResponseEntity<String> createSubmission(@RequestBody SubmissionRequestDto submissionDto) {
        Submission submission = new Submission();

        Optional<Student> student = studentRepository.findById(submissionDto.getStudentId());
        if (student.isEmpty()) {
            return ResponseEntity.badRequest().body("Student with ID " + submissionDto.getStudentId() + " does not exist");
        }

        Optional<Assessment> assessment = assessmentRepository.findById(submissionDto.getAssessmentId());
        if (assessment.isEmpty()) {
            return ResponseEntity.badRequest().body("Assessment with ID " + submissionDto.getAssessmentId() + " does not exist");
        }

        submission.setStudent(student.get());
        submission.setAssessment(assessment.get());

        try {
            submissionRepository.save(submission);
            return ResponseEntity.ok("Submission created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/{submissionId}/grade")
    public ResponseEntity<String> calculateGrade(@PathVariable int submissionId) {
        try {
            boolean result = gradeService.calculateGrade(submissionId);

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
            EmailFormateDto email = new EmailFormateDto();
            email.setTo(user.getEmail());
            email.setSubject("Your Have new grades ");
            email.setEmailBody("Your grades are calculated successfully");
            emailService.sendEmail(email);

            if (result) {
                return ResponseEntity.ok("Grade calculated successfully");

            } else {
                return ResponseEntity.badRequest().body("Failed to calculate grade");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @Transactional
    @PostMapping("/{submissionId}/update-grade")
    public ResponseEntity<String> updateSubmissionGrade( @PathVariable long submissionId, @RequestParam  double grade) {

        Optional<Submission> submissionOptional = submissionRepository.findById(submissionId);
        if (submissionOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Submission not found");
        }

        Submission submission = submissionOptional.get();
        submission.setGrade(grade);

        try {
            submissionRepository.save(submission);
            return ResponseEntity.ok("Submission grade updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}