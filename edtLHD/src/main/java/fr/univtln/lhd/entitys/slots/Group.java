package fr.univtln.lhd.entitys.slots;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Group {
    private final String name;

    private List students; //modify List<Student class>

    public static Group getInstance(String name){
        return new Group(name, new ArrayList<>());
    }

    public static Group getInstance(String name, List students) {
        return new Group(name, students);
    }
}
