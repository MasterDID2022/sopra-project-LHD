package fr.univtln.lhd.model.entities.slots;

import fr.univtln.lhd.exception.IdException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Class defining a Classroom
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Classroom {

    private long id;

    private final String name;
    //private final String buildingName; //? might not be a string in database ?

    /**
     * Factory for a classroom given an id
     * @param id long identifier for a Classroom (default -1)
     * @param name name of the classroom
     * //@param buildingName name of the building for that classroom
     * @return an instance of Classroom class
     */
    public static Classroom getInstance(long id, String name) { return new Classroom(id, name); }

    /**
     * Factory for a classroom
     * @param name name of the classroom
     * //@param buildingName name of the building for that classroom
     * @return an instance of Classroom class
     */
    public static Classroom getInstance(String name) {
        return getInstance(-1, name);
    }

    /**
     * Set the id, should only be used by the DAO
     * @param id long unique identifier
     * @throws IdException id is not allowed to be negative, Exception thrown otherwise
     */
    public void setId(long id) throws IdException {
        if (id<0) {
            throw  new IdException("Id Error");
        }
        this.id = id;
    }

    /**
     * Override of Equal
     * the equality depend on the name
     * @param o Classroom
     * @return True if equal False elif
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Classroom classroom = (Classroom) o;
        return Objects.equals(name, classroom.getName());
    }

    /**
     * Override of hashcode
     * the resulting hashcode depend on the name of the Classroom
     * @return hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
