package fr.univtln.lhd.model.entities.slots;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ClassroomTest {

    public Classroom getInstanceOfClassroom(){
        return Classroom.getInstance("Name");
    }

    @Test
    void testInstanceNotNull(){
        Classroom classroom = getInstanceOfClassroom();
        assertNotNull(classroom);
    }

    @Test
    void testNameEquality(){
        Classroom classroom = getInstanceOfClassroom();
        assertEquals("Name", classroom.getName());
    }
}
