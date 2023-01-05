package fr.univtln.lhd.model.entities.slots;

import fr.univtln.lhd.exceptions.IdException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Class defining a Subject
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
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
        return new Subject(-1, name, hourCountMax);
    }

    public void setId(long id) throws IdException {
        if (id<0) throw new IdException();
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}
