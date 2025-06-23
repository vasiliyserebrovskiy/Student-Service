package ait.cohort5860.student.dao;

import ait.cohort5860.student.model.Student;

import java.util.stream.Stream;

public interface CustomStudentRepository {
    Stream<Student> findByExamAndScoreGreaterThen(String examName, Integer score);
}
