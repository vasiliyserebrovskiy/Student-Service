package ait.cohort5860.student.dao;

import ait.cohort5860.student.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

/**
 * @author Vasilii Serebrovskii
 * @version 1.0 (23.06.2025)
 */
@Repository
@RequiredArgsConstructor
public class CustomStudentRepositoryImpl implements CustomStudentRepository{

    private final MongoTemplate mongoTemplate;

    @Override
    public Stream<Student> findByExamAndScoreGreaterThen(String examName, Integer score) {
        Query query = new Query();
        query.addCriteria(Criteria.where("scores." + examName).gt(score));
        return mongoTemplate.stream(query, Student.class);
    }
}
