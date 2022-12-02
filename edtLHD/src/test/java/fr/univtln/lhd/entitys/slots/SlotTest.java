package fr.univtln.lhd.entitys.slots;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SlotTest {

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

    @Test
    public void testInstanceNotNull () {
        Slot slot = getInstanceOfSlot();
        assertNotNull(slot);
    }
}