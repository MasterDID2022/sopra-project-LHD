package fr.univtln.lhd.model.entities.slots;

import fr.univtln.lhd.exceptions.IdException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class defining a Classroom
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class Classroom {

    private long id;

    private final String name;

    /**
     * Factory for a classroom
     * @param name name of the classroom
     * //@param buildingName name of the building for that classroom
     * @return an instance of Classroom class
     */
    public static Classroom getInstance(String name) {
        return new Classroom(-1, name);
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
