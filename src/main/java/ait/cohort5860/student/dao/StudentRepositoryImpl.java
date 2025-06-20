package ait.cohort5860.student.dao;

import ait.cohort5860.student.model.Student;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Vasilii Serebrovskii
 * @version 1.0 (20.06.2025)
 */
@Component
public class StudentRepositoryImpl implements StudentRepository{
    private static Map<Long, Student> students = new ConcurrentHashMap<>();

    @Override
    public Student save(Student student) {
        students.put(student.getId(), student);
        return student;
    }

    @Override
    public Optional<Student> findById(Long id) {
        return Optional.ofNullable(students.get(id));
    }

    @Override
    public void deleteById(Long id) {
        students.remove(id);
    }

    @Override
    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }
}
