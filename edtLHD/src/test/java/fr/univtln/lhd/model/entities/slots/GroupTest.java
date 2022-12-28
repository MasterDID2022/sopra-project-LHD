package fr.univtln.lhd.model.entities.slots;

import static org.junit.jupiter.api.Assertions.*;

import fr.univtln.lhd.exceptions.IdException;
import org.junit.jupiter.api.Assertions;
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


    @Test
    void ShouldThrowAnIdException(){
        Group group = getInstanceOfGroup();
        IdException thrown = Assertions.assertThrows(
                IdException.class,() ->group.setId(-3),"should throw id error"
        );
        Assertions.assertFalse(thrown.getMessage().contains("should throw id error"));
    }

}