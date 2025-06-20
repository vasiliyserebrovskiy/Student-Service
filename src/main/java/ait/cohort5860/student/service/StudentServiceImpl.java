package ait.cohort5860.student.service;

import ait.cohort5860.student.dao.StudentRepository;
import ait.cohort5860.student.dto.ScoreDto;
import ait.cohort5860.student.dto.StudentCredentialsDto;
import ait.cohort5860.student.dto.StudentDto;
import ait.cohort5860.student.dto.StudentUpdateDto;
import ait.cohort5860.student.dto.exceptions.NotFoundException;
import ait.cohort5860.student.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author Vasilii Serebrovskii
 * @version 1.0 (20.06.2025)
 */
@Component
public class StudentServiceImpl implements StudentService{
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Boolean addStudent(StudentCredentialsDto studentCredentialsDto) {
        if(studentRepository.findById(studentCredentialsDto.getId()).isPresent()) {
            return false;
        }
        Student student = new Student(studentCredentialsDto.getId(), studentCredentialsDto.getName(),
                studentCredentialsDto.getPassword());
        studentRepository.save(student);
        return true;
    }

    @Override
    public StudentDto findStudent(Long id) {
        Student student = studentRepository.findById(id).orElseThrow(NotFoundException::new);
        return new StudentDto(student.getId(), student.getName(), student.getScores());
    }

    @Override
    public StudentDto removeStudent(Long id) {
        return null;
    }

    @Override
    public StudentCredentialsDto updateStudent(Long id, StudentUpdateDto studentUpdateDto) {
        return null;
    }

    @Override
    public Boolean addScore(Long id, ScoreDto scoreDto) {
        return null;
    }

    @Override
    public List<StudentDto> findStudentsByName(String name) {
        return List.of();
    }

    @Override
    public Long countStudentsByNames(Set<String> names) {
        return 0L;
    }

    @Override
    public List<StudentDto> findStudentsByExamNameMinScore(String examName, Integer minScore) {
        return List.of();
    }
}
