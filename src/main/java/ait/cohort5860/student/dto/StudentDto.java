package ait.cohort5860.student.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Vasilii Serebrovskii
 * @version 1.0 (20.06.2025)
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private Long id;
    private String name;
    private Map<String, Integer> scores;
}
