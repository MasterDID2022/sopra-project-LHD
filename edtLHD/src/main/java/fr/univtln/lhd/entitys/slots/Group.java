package fr.univtln.lhd.entitys.slots;

import fr.univtln.lhd.entitys.Student;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Group {
    private final String name;

    private List<Student> students; //modify List<Student class>

    public static Group getInstance(String name){
        return new Group(name, new ArrayList<>());
    }

    public static Group getInstance(String name, List<Student> students) {
        return new Group(name, students);
    }
}
