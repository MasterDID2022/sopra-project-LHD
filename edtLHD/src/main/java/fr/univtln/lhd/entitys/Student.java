package fr.univtln.lhd.entitys;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class Student extends User {

    private Student(String name, String fname, String email) {
        super(name, fname, email);
    }

    public static Student of(String name, String fname, String email) {
        return new Student(name, fname, email);
    }

}
