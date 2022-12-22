package fr.univtln.lhd.model.entities.slots;

import fr.univtln.lhd.exceptions.IdException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * Class for defining a Group
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class Group {

    private long id;

    private final String name;

    /**
     * Factory for a Group
     * @param name Group name
     * @return an instance of a Group
     */
    public static Group getInstance(String name) {
        return new Group(-1, name);
    }

    public void setId(long id) throws IdException {
        if (id<0) throw new IdException();
        this.id = id;
    }
}
