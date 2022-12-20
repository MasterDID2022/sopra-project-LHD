package fr.univtln.lhd.entities.slots;

import static org.junit.jupiter.api.Assertions.*;

import fr.univtln.lhd.model.entities.slots.Subject;
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
        assertEquals( "Name",subject.getName());
    }

    @Test
    void testHourCountMaxEquality(){
        Subject subject = getInstanceOfSubject();
        assertEquals(70, subject.getHourCountMax());
    }
}
