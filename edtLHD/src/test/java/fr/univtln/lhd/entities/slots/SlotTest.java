package fr.univtln.lhd.entities.slots;

import fr.univtln.lhd.model.entities.slots.Classroom;
import fr.univtln.lhd.model.entities.slots.Group;
import fr.univtln.lhd.model.entities.slots.Slot;
import fr.univtln.lhd.model.entities.slots.Subject;
import org.junit.jupiter.api.Test;
import org.threeten.extra.Interval;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    public List<Group> getListOfGroup() {
        List<Group> groups = new ArrayList<>();
        groups.add( getInstanceOfGroup() );
        return groups;
    }

    public Slot getInstanceOfSlot () {
        return Slot.getInstance(Slot.SlotType.CM, getInstanceOfClassroom().getId(), getInstanceOfSubject().getId(), getListOfGroup(), Interval.of(Instant.now(),Instant.now().plusSeconds(10)));
    }

    public Slot getInstanceOfSlotWithMemo () {
        return Slot.getInstance(Slot.SlotType.CM, getInstanceOfClassroom().getId(), getInstanceOfSubject().getId(), getListOfGroup(), Interval.of(Instant.now(),Instant.now().plusSeconds(10)),"memo");
    }

    @Test
    void ShouldBeSettable () {
        Slot slot = getInstanceOfSlot();
        slot.setMemo("memo");
        assertEquals("memo",slot.getMemo());
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