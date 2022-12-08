package fr.univtln.lhd.model.entities.slots;

import fr.univtln.lhd.model.entities.user.Student;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for defining a Group
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Group {
    private final String name;

    private List<Student> students; //modify List<Student class>

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
