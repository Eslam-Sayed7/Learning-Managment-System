package com.Java.LMS.platform.Course;

import com.Java.LMS.platform.adapters.controller.SubmissionController;
import com.Java.LMS.platform.domain.Entities.Submission;
import com.Java.LMS.platform.infrastructure.repository.AssessmentRepository;
import com.Java.LMS.platform.infrastructure.repository.StudentRepository;
import com.Java.LMS.platform.infrastructure.repository.SubmissionRepository;
import com.Java.LMS.platform.service.GradeService;
import com.Java.LMS.platform.service.dto.Course.SubmissionRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SubmissionController.class)
public class SubmissionControllerTest {

    private MockMvc mockMvc;

    private SubmissionRepository submissionRepository;
    private AssessmentRepository assessmentRepository;
    private StudentRepository studentRepository;
    private GradeService gradeService;

    @BeforeEach
    void setup() {
        submissionRepository = Mockito.mock(SubmissionRepository.class);
        assessmentRepository = Mockito.mock(AssessmentRepository.class);
        studentRepository = Mockito.mock(StudentRepository.class);
        gradeService = Mockito.mock(GradeService.class);

        SubmissionController controller = new SubmissionController(submissionRepository, assessmentRepository, studentRepository, gradeService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testCreateSubmission_Success() throws Exception {
        // Arrange
        SubmissionRequestDto submissionDto = new SubmissionRequestDto();
        submissionDto.setAssessmentId(1L);
        submissionDto.setStudentId(1L);

        when(submissionRepository.save(any(Submission.class))).thenReturn(new Submission());

        // Act & Assert
        mockMvc.perform(post("/submissions/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(submissionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    private String asJsonString(Object obj) throws Exception {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
