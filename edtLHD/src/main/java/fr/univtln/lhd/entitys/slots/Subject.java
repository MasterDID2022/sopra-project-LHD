package fr.univtln.lhd.entitys.slots;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Subject {

    private final String name;
    private final int hourCountMax; //max number of hours dedicated to this subject

    public static Subject getInstance(String name, int hourCountMax){
        return new Subject(name, hourCountMax);
    }
}
