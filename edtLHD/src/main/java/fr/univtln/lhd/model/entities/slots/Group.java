package fr.univtln.lhd.model.entities.slots;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.users.Student;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for defining a Group
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class Group {

    private long id;

    private final String name;
    private List<Student> students; //modify List<Student class>

    /**
     * Factory for a Group given a list of students
     * @param name Group name
     * @param students Group students list
     * @return an instance of a Group
     */
    public static Group getInstance(String name, List<Student> students) {
        return new Group(-1, name, students);
    }

    /**
     * Factory for a Group with no students
     * @param name Group name
     * @return an instance of a Group
     */
    public static Group getInstance(String name){
        return getInstance(name, new ArrayList<>());
    }

    /**
     * Take a Student and add it to the List of students of the Group
     * @param student student to be added
     */
    public void add(Student student){
        student.add(this);
        this.students.add(student);
    }

    public void setId(long id) throws IdException {
        if (id<0) throw new IdException();
        this.id = id;
    }
}
