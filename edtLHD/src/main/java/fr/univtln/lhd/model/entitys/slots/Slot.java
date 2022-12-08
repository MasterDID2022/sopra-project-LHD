package fr.univtln.lhd.model.entitys.slots;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Class defining a Slot
 */
@Getter
@Setter
public class Slot {

    public enum SlotType { CM, TD, TP, EXAM, CONFERENCE, REUNION, OTHER }

    private final SlotType type;
    private final Classroom classroom;
    private final Subject subject;
    private final Group group;

    private int timeRange; //time range type needs to be changed to Time Range Wrapper Class

    private String memo; //Slot annotation

    public Slot(SlotType type, Classroom classroom, Subject subject, Group group, int timeRange, String memo) {
        this.type = type;
        this.classroom = classroom;
        this.subject = subject;
        this.group = group;
        this.timeRange = timeRange;
        this.memo = memo;
    }

    /**
     * Factory for a Slot class
     * @param type SlotType enum, defines the type of this Slot
     * @param classroom Classroom object, to which Classroom this Slot is associated with
     * @param subject Subject object, what subject this Slot is about
     * @param group Group object, to what group this Slot is for
     * @param timeRange indicates the Time taken by the Slot
     * @param memo a little annotations for that Slot
     * @return an instance of Slot
     */
    public static Slot getInstance(SlotType type, Classroom classroom, Subject subject, Group group, int timeRange, String memo){
        return new Slot(type, classroom, subject, group, timeRange, memo);
    }

    /**
     * Factory for a Slot class
     * @param type SlotType enum, defines the type of this Slot
     * @param classroom Classroom object, to which Classroom this Slot is associated with
     * @param subject Subject object, what subject this Slot is about
     * @param group Group object, to what group this Slot is for
     * @param timeRange indicates the Time taken by the Slot
     * @return an instance of Slot
     */
    public static Slot getInstance(SlotType type, Classroom classroom, Subject subject, Group group, int timeRange){
        return new Slot(type, classroom, subject, group, timeRange, null);
    }
}
