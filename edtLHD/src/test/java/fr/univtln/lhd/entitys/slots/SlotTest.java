package fr.univtln.lhd.entitys.slots;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SlotTest {

    private Classroom getInstanceOfClassroom() { return Classroom.getInstance("Name", "BuildingName"); }

    private Subject getInstanceOfSubject() { return Subject.getInstance("Name", 70); }

    private Group getInstanceOfGroup() { return Group.getInstance("Name"); }

    private Slot getInstanceOfSlot(){
        return Slot.getInstance(Slot.SlotType.CM, getInstanceOfClassroom(), getInstanceOfSubject(), getInstanceOfGroup(), 10);
    }

    @Test
    void testInstanceNotNull(){
        Slot slot = getInstanceOfSlot();
        assertNotNull(slot);
    }

    @Test
    void testSlotTypeEquality(){
        Slot slot = getInstanceOfSlot();
        assertEquals(Slot.SlotType.CM, slot.getType());
    }

    @Test
    void testClassroomEquality(){
        Classroom classroom = getInstanceOfClassroom();
        Slot slot = getInstanceOfSlot();
        assertEquals(classroom, slot.getClassroom());
    }

    @Test
    void testSubjectEquality(){
        Subject subject = getInstanceOfSubject();
        Slot slot = getInstanceOfSlot();
        assertEquals(subject, slot.getSubject());
    }

    @Test
    void testGroupEquality(){
        Group group = getInstanceOfGroup();
        Slot slot = getInstanceOfSlot();
        assertEquals(group, slot.getGroup());
    }

    @Test
    void testTimeRangeEquality(){
        Slot slot = getInstanceOfSlot();
        assertEquals(10, slot.getTimeRange());
    }
}
