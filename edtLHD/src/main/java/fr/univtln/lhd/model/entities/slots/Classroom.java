package fr.univtln.lhd.model.entities.slots;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Class defining a Classroom
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Classroom {

    private final String name;
    private final String buildingName; //? might not be a string in database ?

    /**
     * Factory for a classroom
     * @param name name of the classroom
     * @param buildingName name of the building for that classroom
     * @return an instance of Classroom class
     */
    public static Classroom getInstance(String name, String buildingName) {
        return new Classroom(name, buildingName);
    }
}
