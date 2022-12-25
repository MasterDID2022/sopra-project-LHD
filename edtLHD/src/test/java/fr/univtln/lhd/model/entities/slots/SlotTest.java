package fr.univtln.lhd.model.entities.slots;

import fr.univtln.lhd.model.entities.users.Professor;
import org.junit.jupiter.api.Test;
import org.threeten.extra.Interval;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SlotTest {

    public Classroom getInstanceOfClassroom () {
        return Classroom.getInstance("Name");
    }

    public Subject getInstanceOfSubject () {
        return Subject.getInstance("Name", 70);
    }

    public Group getInstanceOfGroup () {
        return Group.getInstance("Name");
    }
    public Professor getInstanceOfProfessor(){return Professor.of("Name","fname","email@lhd.org","test");}
    public List<Professor> getListOfProfessor(){return  List.of(getInstanceOfProfessor());}
    public List<Group> getListOfGroup () {return List.of(getInstanceOfGroup());}

    public Slot getInstanceOfSlot () {
        return Slot.getInstance(Slot.SlotType.CM, getInstanceOfClassroom(), getInstanceOfSubject(), getListOfGroup(), getListOfProfessor(),Interval.of(Instant.now(), Instant.now().plusSeconds(10)));
    }

    public Slot getInstanceOfSlotWithMemo () {
        return Slot.getInstance(Slot.SlotType.CM, getInstanceOfClassroom(), getInstanceOfSubject(), getListOfGroup(),getListOfProfessor(),Interval.of(Instant.now(), Instant.now().plusSeconds(10)), "memo");
    }

    @Test
    void ShouldBeSettable () {
        Slot slot = getInstanceOfSlot();
        slot.setMemo("memo");
        assertEquals("memo", slot.getMemo().orElse(null)); // if optional is empty, null wil make the test fail.
    }

    @Test
    void testInstanceNotNull () {
        Slot slot = getInstanceOfSlot();
        assertNotNull(slot);
    }

    @Test
    void testInstanceNotNullWithMemo () {
        Slot slot = getInstanceOfSlotWithMemo();
        assertNotNull(slot);
    }
}