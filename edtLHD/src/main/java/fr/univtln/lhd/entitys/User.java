package fr.univtln.lhd.entitys;

import lombok.Getter;

@Getter
public abstract class User {
    private final String name;
    private final String fname;
    private final String email;

    protected User(String name, String fname, String email) {
        this.name = name;
        this.fname = fname;
        this.email = email;
    }


}
