package fr.univtln.lhd.entitys;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

}
