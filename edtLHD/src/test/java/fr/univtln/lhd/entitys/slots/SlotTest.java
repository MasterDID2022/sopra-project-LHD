package fr.univtln.lhd.entitys.slots;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlotTest {

    private Classroom getInstanceOfClassroom () {
        return Classroom.getInstance("Name", "BuildingName");
    }

    private Subject getInstanceOfSubject () {
        return Subject.getInstance("Name", 70);
    }

    private Group getInstanceOfGroup () {
        return Group.getInstance("Name");
    }


    private Slot getInstanceOfSlot () {
        return Slot.getInstance(Slot.SlotType.CM, getInstanceOfClassroom(), getInstanceOfSubject(), getInstanceOfGroup(), 10);
    }

    private Slot getInstanceOfSlotWithMemo () {
        return Slot.getInstance(Slot.SlotType.CM, getInstanceOfClassroom(), getInstanceOfSubject(), getInstanceOfGroup(), 10,"memo");
    }

    @Test
    void ShouldBeSetable() {
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