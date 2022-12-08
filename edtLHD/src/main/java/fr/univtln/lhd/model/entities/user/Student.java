package fr.univtln.lhd.model.entities.user;

import lombok.Getter;

/**
 * Class of User that is Student
 */
@Getter
public class Student extends User {

    /**
     * Private constructor use by the factory should not be used
     * ref <code>of</code> methode for parameter meaning
     */
    private Student(String name, String fname, String email) {
        super(name, fname, email);
    }

    /**
     * factory of Student
     * @param name name of a student
     * @param fname first name of a student
     * @param email email of a student
     * @return an instance of Student
     */
    public static Student of(String name, String fname, String email) {
        return new Student(name, fname, email);
    }

    /**
     * Take an object and return if the student is equal to it
     * the only important parameter for the test of equality are those of User
     * @param o an object
     * @return a boolean
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /**
     * HashCode of student, like the equality the only important parameter are those of User
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
