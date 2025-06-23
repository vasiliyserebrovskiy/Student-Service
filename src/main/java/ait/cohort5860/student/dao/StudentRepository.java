package ait.cohort5860.student.dao;

import ait.cohort5860.student.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface StudentRepository extends MongoRepository<Student, Long> {
    Stream<Student> findStudentsByNameIgnoreCase(String name);

    @Query("{'scores.Math':{$gt:90}}")
    Stream<Student> findByExamAndScoreGreaterThen(String examName, Integer score);
}
