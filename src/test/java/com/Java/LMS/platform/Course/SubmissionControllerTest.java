//package com.Java.LMS.platform.Course;
//
//import com.Java.LMS.platform.adapters.controller.SubmissionController;
//import com.Java.LMS.platform.domain.Entities.Submission;
//import com.Java.LMS.platform.infrastructure.repository.AssessmentRepository;
//import com.Java.LMS.platform.infrastructure.repository.StudentRepository;
//import com.Java.LMS.platform.infrastructure.repository.SubmissionRepository;
//import com.Java.LMS.platform.service.GradeService;
//import com.Java.LMS.platform.service.dto.Course.SubmissionRequestDto;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import static org.hamcrest.Matchers.any;
//import static org.mockito.ArgumentMatchers.argThat;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class)
//public class SubmissionControllerTest {
//
//    private MockMvc mockMvc;
//
//    @InjectMocks
//    private SubmissionController submissionController;
//
//    @Mock
//    private SubmissionRepository submissionRepository;
//    @Mock
//    private AssessmentRepository assessmentRepository;
//    @Mock
//    private StudentRepository studentRepository;
//    @Mock
//    private GradeService gradeService;
//
//    @BeforeEach
//    void setup() {
//        mockMvc = MockMvcBuilders.standaloneSetup(submissionController).build();
//    }
//
//    @Test
//    public void testCreateSubmission_Success() throws Exception {
//        // Arrange
//        SubmissionRequestDto submissionDto = new SubmissionRequestDto();
//        submissionDto.setAssessmentId(1L);
//        submissionDto.setStudentId(1L);
//
//        Submission submission = new Submission();
//        submission.setId(1L);
//
//        when(submissionRepository.save(argThat(sub -> sub.getAssessment().getId().equals(submissionDto.getAssessmentId()) && sub.getStudent().getStudentId().equals(submissionDto.getStudentId())))).thenReturn(submission);
//
//        // Act & Assert
//        mockMvc.perform(post("/submissions/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(submissionDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L));
//    }
//
//    private String asJsonString(Object obj) throws Exception {
//        return new ObjectMapper().writeValueAsString(obj);
//    }
//}