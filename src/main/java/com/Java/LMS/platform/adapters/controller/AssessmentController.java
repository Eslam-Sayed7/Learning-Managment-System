package com.Java.LMS.platform.adapters.controller;

import com.Java.LMS.platform.domain.Entities.Assessment;
import com.Java.LMS.platform.domain.Entities.AssessmentRequest;
import com.Java.LMS.platform.domain.Entities.AssessmentType;
import com.Java.LMS.platform.domain.Entities.Course;
import com.Java.LMS.platform.infrastructure.repository.AssessmentTypeRepository;
import com.Java.LMS.platform.service.dto.ResourceNotFoundException;
import com.Java.LMS.platform.service.impl.AssessmentService;
import com.Java.LMS.platform.service.impl.AssessmentTypeService;
import com.Java.LMS.platform.service.impl.CourseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @Autowired
    private CourseServiceImpl courseService;

    @Autowired
    private AssessmentTypeService typeService;
    // Get assessments by course ID
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Assessment>> getAssessmentsByCourse(@PathVariable Long courseId) {
        List<Assessment> assessments = assessmentService.getAssessmentsByCourse(courseId);
        return ResponseEntity.ok(assessments);
    }
    @GetMapping("hello")
    public String hello(){
        return "Hello";
    }
    // Get assessment by name
    @GetMapping("/name/{name}")
    public ResponseEntity<Assessment> getAssessmentByName(@PathVariable String name) {
        Optional<Assessment> assessment = assessmentService.getAssessmentByName(name);
        return assessment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PutMapping("update/{id}")// DONE update
    public ResponseEntity<Assessment> updateAssessment(@PathVariable Long id, @RequestBody AssessmentRequest request) {
        // Retrieve the existing Assessment by ID
        Assessment existingAssessment = assessmentService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with ID: " + id));

        // Retrieve Course by ID
        Course course = courseService.getCourseById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + request.getCourseId()));

        // Retrieve AssessmentType by ID
        AssessmentType assessmentType = typeService.getAssessmentTypeById(request.getAssessmentTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("AssessmentType not found with ID: " + request.getAssessmentTypeId()));

        // Update the fields of the existing Assessment
        existingAssessment.setCourse(course);  // Set the updated Course
        existingAssessment.setAssessmentType(assessmentType);  // Set the updated AssessmentType
        existingAssessment.setName(request.getName());  // Set the updated name
        existingAssessment.setDescription(request.getDescription());  // Set the updated description
        existingAssessment.setCreatedAt(LocalDateTime.now());  // Optionally update createdAt if needed

        // Save the updated Assessment
        Assessment updatedAssessment = assessmentService.saveAssessment(existingAssessment);

        // Return the updated Assessment
        return ResponseEntity.ok(updatedAssessment);
    }

    // Get assessments by assessment type ID
    @GetMapping("/type/{typeId}")//DONE get all assessments
    public ResponseEntity<List<Assessment>> getAssessmentsByType(@PathVariable Long typeId) {
        List<Assessment> assessments = assessmentService.getAssessmentsByType(typeId);
        return ResponseEntity.ok(assessments);
    }

    // Create a new assessment
    @PostMapping("/addAssessment") // DONE POST
    public ResponseEntity<Assessment> createAssessment(@RequestBody AssessmentRequest request) {
        // Retrieve Course by ID
        Course course = courseService.getCourseById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with ID: " + request.getCourseId()));

        // Retrieve AssessmentType by ID
        AssessmentType assessmentType = typeService.getAssessmentTypeById(request.getAssessmentTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("AssessmentType not found with ID: " + request.getAssessmentTypeId()));

        Assessment savedAssessment = new Assessment();
        savedAssessment.setCourse(course); // Set the fetched Course
        savedAssessment.setAssessmentType(assessmentType); // Set the fetched AssessmentType
        savedAssessment.setName(request.getName()); // Set the name from request
        savedAssessment.setDescription(request.getDescription()); // Set the description from request
        savedAssessment.setCreatedAt(LocalDateTime.now()); // Set the current time as createdAt

        // Save the Assessment to the database
        savedAssessment = assessmentService.saveAssessment(savedAssessment);

        // Return the saved Assessment
        return ResponseEntity.ok(savedAssessment);
    }

    // Delete an assessment by ID
    @DeleteMapping("/{assessmentId}") // DONE DELETE
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long assessmentId) {
        assessmentService.deleteAssessment(assessmentId);
        return ResponseEntity.noContent().build();
    }
}