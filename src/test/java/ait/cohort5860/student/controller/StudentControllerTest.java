package ait.cohort5860.student.controller;

import ait.cohort5860.student.dto.ScoreDto;
import ait.cohort5860.student.dto.StudentCredentialsDto;
import ait.cohort5860.student.dto.StudentDto;
import ait.cohort5860.student.dto.StudentUpdateDto;
import ait.cohort5860.student.dto.exceptions.NotFoundException;
import ait.cohort5860.student.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Vasilii Serebrovskii
 * @version 1.0 (24.06.2025)
 */
@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    //@MockBean
    @MockitoBean
    private StudentService studentService;

    private StudentDto studentDto;
    private StudentCredentialsDto studentCredentialsDto;
    private StudentUpdateDto studentUpdateDto;
    private ScoreDto scoreDto;
    private final Long studentId = 1000L;
    private final String studentName = "John";
    private final String password = "1234";

    @BeforeEach
    void setUp() {
        // Initialize test data
        HashMap<String, Integer> scores = new HashMap<>();
        scores.put("Math", 90);
        scores.put("Science", 85);

        studentDto = new StudentDto(studentId, studentName, scores);
        studentCredentialsDto = new StudentCredentialsDto(studentId, studentName, password);
        studentUpdateDto = new StudentUpdateDto("UpdatedName", "updatedPassword");
        scoreDto = new ScoreDto();
        // Need to set fields manually since ScoreDto doesn't have a constructor
        try {
            java.lang.reflect.Field examNameField = ScoreDto.class.getDeclaredField("examName");
            java.lang.reflect.Field scoreField = ScoreDto.class.getDeclaredField("score");
            examNameField.setAccessible(true);
            scoreField.setAccessible(true);
            examNameField.set(scoreDto, "History");
            scoreField.set(scoreDto, 95);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testAddStudent() throws Exception {
        // Arrange
        when(studentService.addStudent(any(StudentCredentialsDto.class))).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentCredentialsDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testFindStudent() throws Exception {
        // Arrange
        when(studentService.findStudent(studentId)).thenReturn(studentDto);

        // Act & Assert
        mockMvc.perform(get("/student/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(studentId.intValue())))
                .andExpect(jsonPath("$.name", is(studentName)))
                .andExpect(jsonPath("$.scores.Math", is(90)))
                .andExpect(jsonPath("$.scores.Science", is(85)));
    }

    @Test
    void testFindStudentNotFound() throws Exception {
        // Arrange
        when(studentService.findStudent(studentId)).thenThrow(new NotFoundException());

        // Act & Assert
        mockMvc.perform(get("/student/{id}", studentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRemoveStudent() throws Exception {
        // Arrange
        when(studentService.removeStudent(studentId)).thenReturn(studentDto);

        // Act & Assert
        mockMvc.perform(delete("/student/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(studentId.intValue())))
                .andExpect(jsonPath("$.name", is(studentName)));
    }

    @Test
    void testUpdateStudent() throws Exception {
        // Arrange
        when(studentService.updateStudent(eq(studentId), any(StudentUpdateDto.class)))
                .thenReturn(new StudentCredentialsDto(studentId, "UpdatedName", "updatedPassword"));

        // Act & Assert
        mockMvc.perform(patch("/student/{id}", studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(studentId.intValue())))
                .andExpect(jsonPath("$.name", is("UpdatedName")))
                .andExpect(jsonPath("$.password", is("updatedPassword")));
    }

    @Test
    void testAddScore() throws Exception {
        // Arrange
        when(studentService.addScore(eq(studentId), any(ScoreDto.class))).thenReturn(true);

        // Act & Assert
        mockMvc.perform(patch("/score/student/{id}", studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(scoreDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testFindStudentsByName() throws Exception {
        // Arrange
        List<StudentDto> students = Arrays.asList(
                new StudentDto(1L, studentName, new HashMap<>()),
                new StudentDto(2L, studentName, new HashMap<>())
        );
        when(studentService.findStudentsByName(studentName)).thenReturn(students);

        // Act & Assert
        mockMvc.perform(get("/students/name/{name}", studentName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    void testCountStudentsByNames() throws Exception {
        // Arrange
        when(studentService.countStudentsByNames(any(Set.class))).thenReturn(2L);

        // Act & Assert
        mockMvc.perform(get("/quantity/students")
                .param("names", "John,Jane"))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    void testFindStudentsByExamNameMinScore() throws Exception {
        // Arrange
        List<StudentDto> students = Arrays.asList(
                new StudentDto(1L, "John", new HashMap<>()),
                new StudentDto(2L, "Jane", new HashMap<>())
        );
        when(studentService.findStudentsByExamNameMinScore("Math", 80)).thenReturn(students);

        // Act & Assert
        mockMvc.perform(get("/students/exam/{examName}/minscore/{minScore}", "Math", 80))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }
}
