package fr.univtln.lhd.model.entities.users;

import lombok.Getter;

/**
 *Class that represent a User that is a Lecturer
 */
@Getter
public class Lecturer extends User {
    private final String title;

    /**
     * Private constructor use by the factory should not be used
     * ref <code>of</code> methode for parameter meaning
     */
    private Lecturer(String name, String fname, String email, String title) {
        super(name, fname, email);
        this.title = title;
    }

    /**
     * Factory for a lecturer
     * @param name name of the lecturer
     * @param fname first name of the lecturer
     * @param email email of the lecturer
     * @param title title of the lecturer
     * @return an instance of Lecturer
     */
    public static Lecturer of(String name, String fname, String email, String title) {
        return new Lecturer(name, fname, email, title);
    }
        /**
     * Take an object and return if the lecturer is equal to it
     * the only important parameter for the test of equality are those of User
     * @param o an object
     * @return a boolean
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /**
     * HashCode of lecturer, like the equality the only important parameter are those of User
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
