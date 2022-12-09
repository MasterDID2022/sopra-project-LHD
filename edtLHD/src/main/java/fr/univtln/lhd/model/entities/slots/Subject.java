package fr.univtln.lhd.model.entities.slots;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.OptionalLong;

/**
 * Class defining a Subject
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Subject {

    private long id;

    private final String name;
    private final float hourCountMax; //max number of hours dedicated to this subject

    /**
     * Factory for a Subject
     * @param name name of the subject
     * @param hourCountMax max number of hours dedicated to this subject
     * @return an instance of Subject
     */
    public static Subject getInstance(String name, float hourCountMax){
        return getInstance(-1, name, hourCountMax);
    }

    /**
     * Factory for a Subject
     * @param id long identifier for a Classroom (default -1)
     * @param name name of the subject
     * @param hourCountMax max number of hours dedicated to this subject
     * @return an instance of Subject
     */
    public static Subject getInstance(long id, String name, float hourCountMax) {
        return new Subject(id, name, hourCountMax);
    }

    public void setId(long id){
        this.id = id;
    }

}
