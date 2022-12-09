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

    private long id;

    private final String name;
    //private List<Student> students;

    /**
     * Factory for a Group given a list of students
     * @param id long identifier for a Group (default -1)
     * @param name Group name
     * //@param students Group students list
     * @return an instance of a Group
     */
    public static Group getInstance(long id, String name) {
        return new Group(id, name);
    }

    /**
     * Factory for a Group given a list of students
     * @param name Group name
     * //@param students Group students list
     * @return an instance of a Group
     */
    public static Group getInstance(String name) {
        return new Group(-1, name);
    }

    public void setId(long id){ this.id = id; }
}
