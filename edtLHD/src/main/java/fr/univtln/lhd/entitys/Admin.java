package fr.univtln.lhd.entitys;

import lombok.Getter;

@Getter
public class Admin extends User {
    private final String faculty;

    private Admin(String name, String fname, String email, String faculty) {
        super(name, fname, email);
        this.faculty = faculty;
    }

    public static Admin of(String name, String fname, String email, String UFR) {
        return new Admin(name, fname, email, UFR);
    }
}
