//package com.Java.LMS.platform;
//
//import com.Java.LMS.platform.adapters.controller.CourseManagementController;
//import com.Java.LMS.platform.domain.Entities.Course;
//import com.Java.LMS.platform.domain.Entities.Lesson;
//import com.Java.LMS.platform.domain.Entities.User;
//import com.Java.LMS.platform.service.CourseService;
//import com.Java.LMS.platform.service.LessonService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.boot.test.context.SpringBootTest;
////import org.springframework.boot.test.mock.mockito.SpyBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Collections;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//@SpringBootTest
//@ContextConfiguration(classes = CourseManagementControllerTest.TestConfig.class)
//public class CourseManagementControllerTest {
//
//    @Mock
//    private MockMvc mockMvc;
//
//    private Course testCourse;
//    private Lesson testLesson;
//    private User testUser;
//
//    @BeforeEach
//    public void setup() {
//        TestConfig config = new TestConfig();
//        CourseService courseService = config.courseService();
//        LessonService lessonService = config.lessonService();
//        CourseManagementController controller = new CourseManagementController(courseService, lessonService);
//
//        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
//
//        testCourse = new Course();
//        testCourse.setCourseId(1L);
//        testCourse.setTitle("Test Course");
//        testCourse.setInstructorId(1L);
//
//        testLesson = new Lesson();
//        testLesson.setId(1L);
//        testLesson.setLessonName("Test Lesson");
//        testLesson.setCourse_id(1L);
//
//        testUser = new User();
//        testUser.setUserId(1L);
//        testUser.setUsername("testuser");
//    }
//
//    @Test
//    public void testGetAllCourses() throws Exception {
//        Mockito.when(TestConfig.courseService().getAllCourses())
//                .thenReturn(Collections.singletonList(testCourse));
//
//        mockMvc.perform(get("/api/courses"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].title").value("Test Course"));
//    }
//
//    // Define test-specific configuration for beans
//    @Configuration
//    static class TestConfig {
//
//        @Bean
//        public static CourseService courseService() {
//            return Mockito.mock(CourseService.class);
//        }
//
//        @Bean
//        public LessonService lessonService() {
//            return Mockito.mock(LessonService.class);
//        }
//    }
//}
