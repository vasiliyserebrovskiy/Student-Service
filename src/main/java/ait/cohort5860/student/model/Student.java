package ait.cohort5860.student.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vasilii Serebrovskii
 * @version 1.0 (20.06.2025)
 */
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
//@Document(collection = "students") annotation for naming DB collection in mongoDB
public class Student {
    //@Id - annotation for parameter which must be unique primary key
    private long id;
    @Setter
    private String name;
    @Setter
    private String password;
    private Map<String, Integer> scores = new HashMap<>();

    public Student(long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public boolean addScore(String examName, Integer score) {
        return scores.put(examName, score) == null;
    }
}
