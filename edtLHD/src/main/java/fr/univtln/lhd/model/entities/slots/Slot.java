package fr.univtln.lhd.model.entities.slots;

import lombok.*;
import org.threeten.extra.Interval;

import java.util.List;
import java.util.Optional;

/**
 * Class defining a Slot
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class Slot {

    public enum SlotType { CM, TD, TP, EXAM, CONFERENCE, REUNION, OTHER }

    private long id;

    private final SlotType type;
    private final Classroom  classroom;
    private final Subject subject;
    private final List<Group> group;

    private Interval timeRange; //time range type needs to be changed to Time Range Wrapper Class
    @Getter(AccessLevel.NONE)
    private String memo; //Slot annotation

    public Optional <String> getMemo () {
        return Optional.ofNullable(memo);
    }


    /**
     * Factory for a Slot class
     * @param id long identifier for a Slot (default -1)
     * @param type SlotType enum, defines the type of this Slot
     * @param classroom Classroom object, to which Classroom this Slot is associated with
     * @param subject Subject object, what subject this Slot is about
     * @param group Group object, to what group this Slot is for
     * @param timeRange indicates the Time taken by the Slot
     * @param memo a little annotations for that Slot
     * @return an instance of Slot
     */
    public static Slot getInstance(long id, SlotType type, Classroom classroom, Subject subject, List<Group> group, Interval timeRange, String memo){
        return new Slot(id, type, classroom, subject, group, timeRange, memo);
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
    public static Slot getInstance(SlotType type, Classroom classroom, Subject subject, List<Group> group, Interval timeRange){
        return getInstance(-1, type, classroom, subject, group, timeRange, null);
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
    public static Slot getInstance(SlotType type, Classroom classroom, Subject subject, List<Group> group, Interval timeRange, String memo){
        return getInstance(-1, type, classroom, subject, group, timeRange, memo);
    }
}
