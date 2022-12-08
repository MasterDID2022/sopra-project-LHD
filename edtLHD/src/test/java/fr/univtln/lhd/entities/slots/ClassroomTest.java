package fr.univtln.lhd.entities.slots;

import static org.junit.jupiter.api.Assertions.*;

import fr.univtln.lhd.model.entities.slots.Classroom;
import org.junit.jupiter.api.Test;

class ClassroomTest {

    public Classroom getInstanceOfClassroom(){
        return Classroom.getInstance("Name", "BuildingName");
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

    @Test
    void testBuildingNameEquality(){
        Classroom classroom = getInstanceOfClassroom();
        assertEquals("BuildingName", classroom.getBuildingName());
    }
}
