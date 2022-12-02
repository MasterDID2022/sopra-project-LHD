package fr.univtln.lhd.entitys.slots;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Classroom {

    private final String name;
    private final String buildingName; //? might not be a string in database ?

    public static Classroom getInstance(String name, String buildingName) {
        return new Classroom(name, buildingName);
    }
}
