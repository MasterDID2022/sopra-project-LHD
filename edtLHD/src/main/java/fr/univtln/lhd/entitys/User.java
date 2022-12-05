package fr.univtln.lhd.entitys;

import lombok.Getter;

import java.util.Objects;

@Getter
public abstract class User {
    private final String name;
    private final String fname;
    private final String email;

    /**
     *
     * @param name name of a user
     * @param fname first name of a user
     * @param email email of a user
     */
    protected User(String name, String fname, String email) {
        this.name = name;
        this.fname = fname;
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(fname, user.fname) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, fname, email);
    }
}
