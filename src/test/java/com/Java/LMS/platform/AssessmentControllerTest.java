package com.Java.LMS.platform;

import com.Java.LMS.platform.adapters.controller.AssessmentController;
import com.Java.LMS.platform.domain.Entities.*;
import com.Java.LMS.platform.service.impl.AssessmentServiceImpl;
import com.Java.LMS.platform.service.impl.CourseServiceImpl;
import com.Java.LMS.platform.service.impl.QuestionService;
import com.Java.LMS.platform.service.impl.AssessmentTypeService;
import com.Java.LMS.platform.service.dto.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssessmentControllerTest {

    @InjectMocks
    private AssessmentController assessmentController;

    @Mock
    private AssessmentServiceImpl assessmentService;

    @Mock
    private CourseServiceImpl courseService;

    @Mock
    private AssessmentTypeService typeService;

    @Mock
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // --- CREATE ASSESSMENT ---
    @Test
    void createAssessment_ShouldReturnCreatedAssessment_WhenValidRequest() {
        AssessmentRequest request = new AssessmentRequest();
        request.setName("Test Assessment");
        request.setDescription("Description");
        request.setCourseId(1L);
        request.setAssessmentTypeId(1L);

        Course course = new Course();
        course.setCourseId(1L);

        AssessmentType assessmentType = new AssessmentType();
        assessmentType.setId(1L);

        Assessment savedAssessment = new Assessment();
        savedAssessment.setId(1L);
        savedAssessment.setName("Test Assessment");
        savedAssessment.setDescription("Description");
        savedAssessment.setCourse(course);
        savedAssessment.setAssessmentType(assessmentType);
        savedAssessment.setCreatedAt(LocalDateTime.now());

        when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
        when(typeService.getAssessmentTypeById(1L)).thenReturn(Optional.of(assessmentType));
        when(assessmentService.saveAssessment(any(Assessment.class))).thenReturn(savedAssessment);

        ResponseEntity<Assessment> response = assessmentController.createAssessment(request);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Test Assessment", response.getBody().getName());
    }

    // --- GET ASSESSMENTS BY COURSE ---
    @Test
    void getAssessmentsByCourse_ShouldReturnAssessments_WhenCourseExists() {
        Long courseId = 1L;
        Assessment assessment = new Assessment();
        assessment.setName("Test Assessment");

        when(assessmentService.getAssessmentsByCourse(courseId)).thenReturn(List.of(assessment));

        ResponseEntity<List<Assessment>> response = assessmentController.getAssessmentsByCourse(courseId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Assessment", response.getBody().get(0).getName());
    }

    @Test
    void getAssessmentsByCourse_ShouldReturnEmptyList_WhenNoAssessments() {
        Long courseId = 1L;

        when(assessmentService.getAssessmentsByCourse(courseId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<Assessment>> response = assessmentController.getAssessmentsByCourse(courseId);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().isEmpty());
    }

    // --- GET RANDOMIZED QUESTIONS PER ATTEMPT ---
    @Test
    void getRandomizedQuestionsPerAttempt_ShouldReturnQuestions_WhenValidAssessment() {
        Long assessmentId = 1L;

        Assessment assessment = new Assessment();
        assessment.setId(assessmentId);
        assessment.setAssessmentType(new AssessmentType());
        assessment.getAssessmentType().setId(1L); // Setting type ID to 1 for randomization check

        List<Questions> questions = Arrays.asList(new Questions(), new Questions());

        when(assessmentService.findById(assessmentId)).thenReturn(Optional.of(assessment));
        when(questionService.getRandomQuestions(assessmentId)).thenReturn(questions);

        ResponseEntity<List<Questions>> response = assessmentController.getRanomizedQuestionsPerAttempt(assessmentId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getRandomizedQuestionsPerAttempt_ShouldReturnBadRequest_WhenInvalidAssessmentType() {
        Long assessmentId = 1L;

        Assessment assessment = new Assessment();
        assessment.setId(assessmentId);
        assessment.setAssessmentType(new AssessmentType());
        assessment.getAssessmentType().setId(2L); // Setting type ID to non-randomizable type

        when(assessmentService.findById(assessmentId)).thenReturn(Optional.of(assessment));

        ResponseEntity<List<Questions>> response = assessmentController.getRanomizedQuestionsPerAttempt(assessmentId);

        assertEquals(400, response.getStatusCodeValue());
    }

    // --- DELETE ASSESSMENT ---
    @Test
    void deleteAssessment_ShouldReturnNoContent_WhenAssessmentDeleted() {
        Long assessmentId = 1L;

        doNothing().when(assessmentService).deleteAssessment(assessmentId);

        ResponseEntity<Void> response = assessmentController.deleteAssessment(assessmentId);

        assertEquals(204, response.getStatusCodeValue());
        verify(assessmentService, times(1)).deleteAssessment(assessmentId);
    }

    // --- UPDATE ASSESSMENT ---
    @Test
    void updateAssessment_ShouldReturnUpdatedAssessment_WhenValidRequest() {
        Long assessmentId = 1L;
        AssessmentRequest request = new AssessmentRequest();
        request.setName("Updated Assessment");
        request.setDescription("Updated Description");
        request.setCourseId(1L);
        request.setAssessmentTypeId(1L);

        Course course = new Course();
        course.setCourseId(1L);

        AssessmentType assessmentType = new AssessmentType();
        assessmentType.setId(1L);

        Assessment existingAssessment = new Assessment();
        existingAssessment.setId(assessmentId);
        existingAssessment.setName("Old Name");

        Assessment updatedAssessment = new Assessment();
        updatedAssessment.setId(assessmentId);
        updatedAssessment.setName("Updated Assessment");

        when(assessmentService.findById(assessmentId)).thenReturn(Optional.of(existingAssessment));
        when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
        when(typeService.getAssessmentTypeById(1L)).thenReturn(Optional.of(assessmentType));
        when(assessmentService.saveAssessment(any(Assessment.class))).thenReturn(updatedAssessment);

        ResponseEntity<Assessment> response = assessmentController.updateAssessment(assessmentId, request);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Updated Assessment", response.getBody().getName());
    }

    @Test
    void updateAssessment_ShouldThrowNotFound_WhenAssessmentNotFound() {
        Long assessmentId = 1L;
        AssessmentRequest request = new AssessmentRequest();

        when(assessmentService.findById(assessmentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            assessmentController.updateAssessment(assessmentId, request);
        });

        assertTrue(exception.getMessage().contains("Assessment not found"));
    }
}
