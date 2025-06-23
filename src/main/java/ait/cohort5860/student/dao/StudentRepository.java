package ait.cohort5860.student.dao;

import ait.cohort5860.student.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Set;
import java.util.stream.Stream;

public interface StudentRepository extends MongoRepository<Student, Long>, CustomStudentRepository {

    Stream<Student> findStudentsByNameIgnoreCase(String name);

    Long countByNameInIgnoreCase(Set<String> names);

    // @Query("{'scores.Math':{$gt:90}}")
    //Stream<Student> findByExamAndScoreGreaterThen(String examName, Integer score);

    // @Query("{'scores.Math':{$gt:?0}}")
    //Stream<Student> findByExamAndScoreGreaterThen(Integer score);

}
