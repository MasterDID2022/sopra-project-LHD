package fr.univtln.lhd.entitys;

import lombok.Getter;

@Getter
public class Lecturer extends User {
    private final String title;

    private Lecturer(String name, String fname, String email, String title) {
        super(name, fname, email);
        this.title = title;
    }

    public static Lecturer createLecturer(String name, String fname, String email, String title) {
        return new Lecturer(name, fname, email, title);
    }
}
