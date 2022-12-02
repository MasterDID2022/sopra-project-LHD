package fr.univtln.lhd.entitys.slots;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class Slot {

    private enum SlotType { CM, TD, TP, EXAM, CONFERENCE, REUNION, OTHER }

    private final SlotType type;
    private final Classroom classroom;
    private final Subject subject;
    private final Group group;

    private int timeRange; //time range type needs to be changed to Time Range Wrapper Class

    private String memo; //Slot annotation

    public static Slot getInstance(SlotType type, Classroom classroom, Subject subject, Group group, int timeRange, String memo){
        return new Slot(type, classroom, subject, group, timeRange, memo);
    }

    public static Slot getInstance(SlotType type, Classroom classroom, Subject subject, Group group, int timeRange){
        return new Slot(type, classroom, subject, group, timeRange, null);
    }
}
