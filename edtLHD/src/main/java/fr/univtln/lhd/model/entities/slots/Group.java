package fr.univtln.lhd.model.entities.slots;

import fr.univtln.lhd.exceptions.IdException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Class for defining a Group
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Group {
    private long id;
    private final String name;

    /**
     * Factory for a Group given a list of students
     * @param id long identifier for a Group (default -1)
     * @param name Group name
     * //@param students Group students list
     * @return an instance of a Group
     */
    public static Group getInstance(long id, String name) {
        return new Group(id, name);
    }

    /**
     * Factory for a Group given a list of students
     * @param name Group name
     * //@param students Group students list
     * @return an instance of a Group
     */
    public static Group getInstance(String name) {
        return new Group(-1, name);
    }

    /**
     * Set the id, should only be used by the DAO
     * @param id
     * @throws IdException
     */
    public void setId(long id) throws IdException {
        if (id<0) {
            throw  new IdException("Id Error");
        }
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id == group.id && Objects.equals(name, group.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
