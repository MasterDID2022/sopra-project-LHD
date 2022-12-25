package fr.univtln.lhd.model.entities.slots;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GroupTest {


    public Group getInstanceOfGroup() {
        return Group.getInstance("Name");
    }

    @Test
    void testInstanceNotNull(){
        Group group = getInstanceOfGroup();
        assertNotNull(group);
    }

    @Test
    void testNameEquality(){
        Group group = getInstanceOfGroup();
        assertEquals("Name", group.getName());
    }
}