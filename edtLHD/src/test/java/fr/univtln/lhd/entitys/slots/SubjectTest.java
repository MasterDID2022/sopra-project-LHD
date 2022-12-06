package fr.univtln.lhd.entitys.slots;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SubjectTest {

    public Subject getInstanceOfSubject(){
        return Subject.getInstance("Name", 70);
    }

    @Test
    void testInstanceNotNull(){
        Subject subject = getInstanceOfSubject();
        assertNotNull(subject);
    }

    @Test
    void testNameEquality(){
        Subject subject = getInstanceOfSubject();
        assertEquals(subject.getName(), "Name");
    }

    @Test
    void testHourCountMaxEquality(){
        Subject subject = getInstanceOfSubject();
        assertEquals(subject.getHourCountMax(), 70);
    }
}
