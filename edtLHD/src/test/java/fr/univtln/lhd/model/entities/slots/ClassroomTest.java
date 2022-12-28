package fr.univtln.lhd.model.entities.slots;

import static org.junit.jupiter.api.Assertions.*;

import fr.univtln.lhd.exceptions.IdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClassroomTest {

    public Classroom getInstanceOfClassroom(){
        return Classroom.getInstance("Name");
    }

    public Classroom getAnotherInstanceOfClassroom(){
        return Classroom.getInstance("Name2");
    }
    @Test
    void testInstanceNotNull(){
        Classroom classroom = getInstanceOfClassroom();
        assertNotNull(classroom);
    }

    @Test
    void ShouldThrowAnIdException(){
        Classroom classroom = getInstanceOfClassroom();
        IdException thrown = Assertions.assertThrows(
                IdException.class,() ->classroom.setId(-3),"should throw id error"
        );
        Assertions.assertFalse(thrown.getMessage().contains("should throw id error"));
    }

    @Test
    void testNameEquality(){
        Classroom classroom = getInstanceOfClassroom();
        assertEquals("Name", classroom.getName());
    }


    @Test
    void HashCodeIsReflexive2(){
        Classroom classroom1 = this.getInstanceOfClassroom();
        Classroom classroom2 = this.getAnotherInstanceOfClassroom();
        Assertions.assertNotEquals(classroom1.hashCode(),classroom2.hashCode());
    }

    @Test
    void differentClassShouldNotBeEqual(){
        Group different = Group.getInstance("Name");
        Classroom classroom = this.getAnotherInstanceOfClassroom();
        Assertions.assertNotEquals(classroom,different);

    }

    @Test
    void EqualIsReflexive2(){
        Classroom classroom1 = this.getInstanceOfClassroom();
        Classroom classroom2 = this.getAnotherInstanceOfClassroom();
        Assertions.assertNotEquals(classroom1,classroom2);
    }

    @Test
    void EqualIsSymmetric(){
        Classroom classroom1 = this.getInstanceOfClassroom();
        Classroom classroom2 = this.getInstanceOfClassroom();
        Assertions.assertTrue(classroom1.equals(classroom2)&&classroom2.equals(classroom1));
    }

    @Test
    void HashCodeIsSymmetric(){
        Classroom classroom1 = this.getInstanceOfClassroom();
        Classroom classroom2 = this.getInstanceOfClassroom();
        Assertions.assertTrue(classroom1.hashCode()==classroom2.hashCode()&&classroom2.hashCode()==classroom1.hashCode());
    }

    @Test
    void HashCodeIsSymmetric2(){
        Classroom classroom1 = this.getInstanceOfClassroom();
        Classroom classroom2 = this.getAnotherInstanceOfClassroom();
        Assertions.assertFalse(classroom1.hashCode()==classroom2.hashCode()&&classroom2.hashCode()==classroom1.hashCode());
    }

    @Test
    void EqualIsSymmetric2(){
        Classroom classroom1 = this.getInstanceOfClassroom();
        Classroom classroom2 = this.getAnotherInstanceOfClassroom();
        Assertions.assertFalse(classroom1.equals(classroom2)&&classroom2.equals(classroom1));
    }

    @Test
    void EqualIsTransitive(){
        Classroom classroom1 = this.getInstanceOfClassroom();
        Classroom classroom2 = this.getInstanceOfClassroom();
        Classroom classroom3 = this.getInstanceOfClassroom();
        Assertions.assertTrue(classroom1.equals(classroom2)&&classroom2.equals(classroom3)&&classroom1.equals(classroom3));
    }
    @Test
    void EqualIsTransitive2(){
        Classroom classroom1 = this.getInstanceOfClassroom();
        Classroom classroom2 = this.getAnotherInstanceOfClassroom();
        Classroom classroom3 = this.getInstanceOfClassroom();
        Assertions.assertFalse(classroom1.equals(classroom2)&&classroom2.equals(classroom3)&&classroom1.equals(classroom3));
    }

    @Test
    void HashCodeIsTransitive(){
        Classroom classroom1 = this.getInstanceOfClassroom();
        Classroom classroom2 = this.getInstanceOfClassroom();
        Classroom classroom3 = this.getInstanceOfClassroom();
        Assertions.assertTrue(classroom1.hashCode()==classroom2.hashCode()&&
                classroom2.hashCode()==classroom3.hashCode()&&
                classroom1.hashCode()==classroom3.hashCode());
    }

    @Test
    void HashCodeIsTransitive2(){
        Classroom classroom1 = this.getInstanceOfClassroom();
        Classroom classroom2 = this.getAnotherInstanceOfClassroom();
        Classroom classroom3 = this.getInstanceOfClassroom();
        Assertions.assertFalse(classroom1.hashCode()==classroom2.hashCode()&&
                classroom2.hashCode()==classroom3.hashCode()&&
                classroom1.hashCode()==classroom3.hashCode());
    }

    @Test
    void ShoudlReturnToSTringOfClassroom(){
        Assertions.assertEquals("Classroom(id=-1, name=Name)",
                getInstanceOfClassroom().toString());
    }

    @Test
    void NullEquality(){
        Classroom classroom1 = this.getInstanceOfClassroom();
        Assertions.assertNotEquals(null,classroom1);
    }


}
