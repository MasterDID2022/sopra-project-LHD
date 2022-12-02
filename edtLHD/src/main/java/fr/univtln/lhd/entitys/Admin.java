package fr.univtln.lhd.entitys;

import lombok.Getter;

@Getter
public class Admin extends User {
    private final String UFR;

    private Admin(String name, String fname, String email, String UFR) {
        super(name, fname, email);
        this.UFR = UFR;
    }

    public static Admin of(String name, String fname, String email, String UFR) {
        return new Admin(name, fname, email, UFR);
    }
}
