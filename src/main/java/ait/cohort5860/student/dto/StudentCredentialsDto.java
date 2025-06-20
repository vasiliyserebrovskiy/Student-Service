package ait.cohort5860.student.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Vasilii Serebrovskii
 * @version 1.0 (20.06.2025)
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudentCredentialsDto {
        private Long id;
        private String name;
        private String password;
}
