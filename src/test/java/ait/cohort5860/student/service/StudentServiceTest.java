package ait.cohort5860.student.service;

import ait.cohort5860.configuration.ServiceConfiguration;
import ait.cohort5860.student.dao.StudentRepository;
import ait.cohort5860.student.dto.ScoreDto;
import ait.cohort5860.student.dto.StudentCredentialsDto;
import ait.cohort5860.student.dto.StudentDto;
import ait.cohort5860.student.dto.StudentUpdateDto;
import ait.cohort5860.student.dto.exceptions.NotFoundException;
import ait.cohort5860.student.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 * @author Vasilii Serebrovskii
 * @version 1.0 (24.06.2025)
 */
@ContextConfiguration(classes = {ServiceConfiguration.class})
@SpringBootTest
public class StudentServiceTest {
    private final long studentId = 1000L;
    private Student student;
    private final String name = "John";
    private final String password = "1234";

    @Autowired
    private ModelMapper modelMapper;

    @MockitoBean // studentRepository will be not real mock repo
    private StudentRepository studentRepository;
    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        student = new Student(studentId, name, password);
        studentService = new StudentServiceImpl(studentRepository, modelMapper);
    }

    @Test
    void testAddStudentWhenStudentDoesNotExist() {
        //Arrange
        StudentCredentialsDto dto = new StudentCredentialsDto(studentId, name, password);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // Act
        boolean result = studentService.addStudent(dto);

        //Assert
        assertTrue(result);

    }

    @Test
    void testAddStudentWhenStudentExists() {
        //Arrange
        StudentCredentialsDto dto = new StudentCredentialsDto(studentId, name, password);
        when(studentRepository.existsById(dto.getId())).thenReturn(true);

        // Act
        boolean result = studentService.addStudent(dto);

        //Assert
        assertFalse(result);
        verify(studentRepository, never()).save(any(Student.class));

    }

    @Test
    void testFindStudentWhenStudentExists() {
        // Arrange
        when(studentRepository.findById(studentId)).thenReturn(Optional.ofNullable(student));

        //Act
        StudentDto studentDto = studentService.findStudent(studentId);

        //Assert
        assertNotNull(studentDto);
        assertEquals(studentId, studentDto.getId());
    }

    @Test
    void testFindStudentWhenStudentNotExists() {
        // Arrange
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        //Act & Assert
        assertThrows(NotFoundException.class, () -> studentService.findStudent(studentId));
    }

    @Test
    void removeStudentWhenStudentExists() {
        // Arrange
        when(studentRepository.findById(studentId)).thenReturn(Optional.ofNullable(student));
        // Act
        StudentDto studentDto = studentService.removeStudent(studentId);

        // Assert
        assertNotNull(studentDto);
        assertEquals(studentId, studentDto.getId());
        verify(studentRepository, times(1)).deleteById(studentId);
    }

    @Test
    void testUpdateStudent() {
        // Arrange
        String newName = "NewName";
        when(studentRepository.findById(studentId)).thenReturn(Optional.ofNullable(student));
        StudentUpdateDto dto = new StudentUpdateDto(newName, null);

        // Act
        StudentCredentialsDto studentCredentialsDto = studentService.updateStudent(studentId, dto);

        // Assert
        assertNotNull(studentCredentialsDto);
        assertEquals(studentId, studentCredentialsDto.getId());
        assertEquals(newName, studentCredentialsDto.getName());
        assertEquals(password, studentCredentialsDto.getPassword());
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testAddScore() {
        // Arrange
        when(studentRepository.findById(studentId)).thenReturn(Optional.ofNullable(student));
        ScoreDto scoreDto = new ScoreDto("History", 95);

        // Act
        boolean result = studentService.addScore(studentId, scoreDto);

        // Assert
        assertTrue(result);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testFindStudentsByName() {
        // Arrange

        String name = "John";
        // Create our student for database return
        Student student1 = new Student(1000L, "John", "1234");
        Student student2 = new Student(2000L, "john", "4321");
        // adding some score
        student1.addScore("History", 95);
        student2.addScore("History", 90);

        // Create a list to use with return as a stream
        List<Student> students = List.of(student1, student2);
        // DB response
        when(studentRepository.findStudentsByNameIgnoreCase(name)).thenReturn(students.stream());

        // Act
        List<StudentDto> studentDto = studentService.findStudentsByName(name);

        // Assert
        assertNotNull(studentDto);
        assertEquals(2, studentDto.size());
        assertEquals(student1.getId(), studentDto.get(0).getId());
        assertEquals(student2.getId(), studentDto.get(1).getId());
        assertEquals(student1.getName(), studentDto.get(0).getName());
        assertEquals(student2.getName(), studentDto.get(1).getName());
        assertEquals(student1.getScores().get("History"), studentDto.get(0).getScores().get("History"));
        assertEquals(student2.getScores().get("History"), studentDto.get(1).getScores().get("History"));
        verify(studentRepository, times(1)).findStudentsByNameIgnoreCase(name);
    }

    @Test
    void testCountStudentsByNames() {
        // Arrange
        Set<String> names = Set.of("Peter", "Mary");
        Long expectedCount = 2L;
        when(studentRepository.countByNameInIgnoreCase(names)).thenReturn(expectedCount);

        // Act
        Long result = studentService.countStudentsByNames(names);

        // Assert
        assertNotNull(result);
        assertEquals(expectedCount, result);
        verify(studentRepository, times(1)).countByNameInIgnoreCase(names);
    }

    @Test
    void testFindStudentsByExamNameMinScore() {
        // Arrange
        String examName = "History";
        int minScore = 80;
        Student student1 = new Student(1000L, "John", "1234");
        Student student2 = new Student(2000L, "john", "4321");
        student1.addScore(examName, 95);
        student2.addScore(examName, 90);

        List<Student> students = List.of(student1, student2);

        when(studentRepository.findByExamAndScoreGreaterThen(examName, minScore)).thenReturn(students.stream());

        // Act
        List<StudentDto> studentDto = studentService.findStudentsByExamNameMinScore(examName, minScore);

        // Assert
        assertNotNull(studentDto);
        assertEquals(2, studentDto.size());
        assertEquals(student1.getId(), studentDto.get(0).getId());
        assertEquals(student2.getId(), studentDto.get(1).getId());
        assertEquals(student1.getName(), studentDto.get(0).getName());
        assertEquals(student2.getName(), studentDto.get(1).getName());
        assertEquals(student1.getScores().get(examName), studentDto.get(0).getScores().get(examName));
        assertEquals(student2.getScores().get(examName), studentDto.get(1).getScores().get(examName));
        assertTrue(studentDto.get(0).getScores().get(examName) > minScore);
        assertTrue(studentDto.get(1).getScores().get(examName) > minScore);
        verify(studentRepository, times(1)).findByExamAndScoreGreaterThen(examName, minScore);
    }
}
