package fr.univtln.lhd.model.entities.slots;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.users.Professor;
import lombok.*;
import org.threeten.extra.Interval;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private final Set<Group> group;
    private final Set<Professor> professors;
    private Interval timeRange;
    @Getter(AccessLevel.NONE)
    private String memo; //Slot annotation

    public Optional <String> getMemo () {
        return Optional.ofNullable(memo);
    }

    /**
     * Factory for a Slot class
     * @param type SlotType enum, defines the type of this Slot
     * @param classroom Classroom object, to which Classroom this Slot is associated with
     * @param subject Subject object, what subject this Slot is about
     * @param professors the Professor(s) teaching the course
     * @param group Group object, to what group this Slot is for
     * @param timeRange indicates the Time taken by the Slot
     * @return an instance of Slot
     */
    public static Slot getInstance(SlotType type, Classroom classroom, Subject subject, Set<Group> group, Set<Professor> professors, Interval timeRange,  String memo){
        return new Slot(-1, type, classroom, subject, group, professors,timeRange, memo);
    }

    /**
     * Factory for a Slot class
     * @param type SlotType enum, defines the type of this Slot
     * @param classroom Classroom object, to which Classroom this Slot is associated with
     * @param subject Subject object, what subject this Slot is about
     * @param group Group object, to what group this Slot is for
     * @param professors the professors teaching the course
     * @param timeRange indicates the Time taken by the Slot
     * @return an instance of Slot
     */
    public static Slot getInstance(SlotType type, Classroom classroom, Subject subject, Set<Group> group,Set<Professor> professors, Interval timeRange){
        return getInstance(type, classroom, subject, group, professors,timeRange, null);
    }

    public void setId(long id) throws IdException {
        if (id<0) throw new IdException();
        this.id = id;
    }

    /**
     * Take a LocalTime and return a String representing this LocalTime
     * eg: 7h58m23 -> 07:58
     * @param time a LocalTime
     * @return Formated String
     */
    private String formatLocalTimeToString(LocalTime time) {
    return DateTimeFormatter.ofPattern("HH:mm").format(time);
    }

    /**
     * Return a formatted String of an interval using
     * the function <code>formatLocalTimeToString</code>
     * @return Formatted string of an Interval
     */
    public String getDisplayTimeInterval() {
        String startTime = formatLocalTimeToString( LocalTime.ofInstant(timeRange.getStart(), ZoneId.systemDefault()) );
        String endTime = formatLocalTimeToString( LocalTime.ofInstant(timeRange.getEnd(), ZoneId.systemDefault()) );
        return startTime + " - " + endTime;
    }
}
