package fr.univtln.lhd.model.entitys.slots;

import fr.univtln.lhd.model.entitys.user.Student;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for defining a Group
 */
@Getter
public class Group {
    private final String name;

    private List<Student> students; //modify List<Student class>

    public Group(String name, List<Student> students) {
        this.name = name;
        this.students = students;
    }

    /**
     * Factory for a Group with no students
     * @param name Group name
     * @return an instance of a Group
     */
    public static Group getInstance(String name){
        return new Group(name, new ArrayList<>());
    }

    /**
     * Factory for a Group given a list of students
     * @param name Group name
     * @param students Group students list
     * @return an instance of a Group
     */
    public static Group getInstance(String name, List<Student> students) {
        return new Group(name, students);
    }
}
