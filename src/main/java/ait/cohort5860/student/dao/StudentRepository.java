package ait.cohort5860.student.dao;

import ait.cohort5860.student.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Set;
import java.util.stream.Stream;


//two variants: with request and custom repository
public interface StudentRepository extends MongoRepository<Student, Long> {//, CustomStudentRepository {

    Stream<Student> findStudentsByNameIgnoreCase(String name);

    Long countByNameInIgnoreCase(Set<String> names);

    // @Query("{'scores.Math':{$gt:90}}")
    //Stream<Student> findByExamAndScoreGreaterThen(String examName, Integer score);

     @Query("{'scores.?0':{$gt:?1}}")
    Stream<Student> findByExamAndScoreGreaterThen(String examName, Integer score);

}
