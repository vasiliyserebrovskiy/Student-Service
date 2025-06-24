package ait.cohort5860.student.service;

import ait.cohort5860.configuration.ServiceConfiguration;
import ait.cohort5860.student.dao.StudentRepository;
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

import java.util.Optional;

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


}
