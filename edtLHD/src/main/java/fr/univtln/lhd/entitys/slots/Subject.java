package fr.univtln.lhd.entitys.slots;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Class defining a Subject
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Subject {

    private final String name;
    private final int hourCountMax; //max number of hours dedicated to this subject

    /**
     * Factory for a Subject
     * @param name name of the subject
     * @param hourCountMax max number of hours dedicated to this subject
     * @return an instance of Subject
     */
    public static Subject getInstance(String name, int hourCountMax){
        return new Subject(name, hourCountMax);
    }
}
