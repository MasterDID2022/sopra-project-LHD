package fr.univtln.lhd.model.entitys.slots;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Class defining a Subject
 */
@Getter
public class Subject {

    private final String name;
    private final int hourCountMax; //max number of hours dedicated to this subject

    public Subject(String name, int hourCountMax) {
        this.name = name;
        this.hourCountMax = hourCountMax;
    }

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
