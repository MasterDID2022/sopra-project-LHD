package fr.univtln.lhd.model.entities.users;

import lombok.Getter;



/**
 *Class that represent a User that is an Admin
 */
@Getter
public class Admin extends User {
    private final String faculty;

    /**
     * Private constructor use by the factory should not be used
     * ref <code>of</code> methode for parameter meaning
     */
    private Admin(String name, String fname, String email, String faculty) {
        super(name, fname, email);
        this.faculty = faculty;
    }

    /**
     * Factory for an admin
     * @param name the name of the admin
     * @param fname the firstname of the admin
     * @param email the email of admin
     * @param faculty the falcuty that the admin manage
     */
    public static Admin of(String name, String fname, String email, String faculty) {
        return new Admin(name, fname, email, faculty);
    }

    /**
     * Take an object and return if the admin is equal to it
     * the only important parameter for the test of equality are those of User
     * @param o an object
     * @return a boolean
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /**
     * HashCode of admin, like the equality the only important parameter are those User
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
