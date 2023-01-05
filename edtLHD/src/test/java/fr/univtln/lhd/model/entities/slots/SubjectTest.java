package fr.univtln.lhd.model.entities.slots;

import static org.junit.jupiter.api.Assertions.*;

import fr.univtln.lhd.exceptions.IdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubjectTest {

    public Subject getInstanceOfSubject(){
        return Subject.getInstance("Name", 70);
    }

    public Subject getAnotherInstanceOfSubject(){
        return Subject.getInstance("Name2", 35);
    }
    @Test
    void testInstanceNotNull(){
        Subject subject = getInstanceOfSubject();
        assertNotNull(subject);
    }


    @Test
    void ShouldThrowAnIdException(){
        Subject subject = getInstanceOfSubject();
        IdException thrown = Assertions.assertThrows(
                IdException.class,() ->subject.setId(-3),"should throw id error"
        );
        Assertions.assertFalse(thrown.getMessage().contains("should throw id error"));
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

    @Test
    void EqualIsReflexive(){
        Subject subject1 = this.getInstanceOfSubject();
        Assertions.assertEquals(subject1,subject1);
    }

    @Test
    void HashCodeIsReflexive(){
        Subject subject1 = this.getInstanceOfSubject();
        Assertions.assertEquals(subject1.hashCode(),subject1.hashCode());
    }

    @Test
    void ShouldGetName(){
        Subject subject1 = getInstanceOfSubject();
        Assertions.assertEquals("Name",subject1.getName());
    }


    @Test
    void HashCodeIsReflexive2(){
        Subject subject1 = this.getInstanceOfSubject();
        Subject subject2 = this.getAnotherInstanceOfSubject();
        Assertions.assertNotEquals(subject1.hashCode(),subject2.hashCode());
    }

    @Test
    void differentClassShouldNotBeEqual(){
        Group different = Group.getInstance("Name");
        Subject subject = this.getAnotherInstanceOfSubject();
        Assertions.assertNotEquals(subject,different);

    }

    @Test
    void EqualIsReflexive2(){
        Subject subject1 = this.getInstanceOfSubject();
        Subject subject2 = this.getAnotherInstanceOfSubject();
        Assertions.assertNotEquals(subject1,subject2);
    }

    @Test
    void EqualIsSymmetric(){
        Subject subject1 = this.getInstanceOfSubject();
        Subject subject2 = this.getInstanceOfSubject();
        Assertions.assertTrue(subject1.equals(subject2)&&subject2.equals(subject1));
    }

    @Test
    void HashCodeIsSymmetric(){
        Subject subject1 = this.getInstanceOfSubject();
        Subject subject2 = this.getInstanceOfSubject();
        Assertions.assertTrue(subject1.hashCode()==subject2.hashCode()&&subject2.hashCode()==subject1.hashCode());
    }

    @Test
    void HashCodeIsSymmetric2(){
        Subject subject1 = this.getInstanceOfSubject();
        Subject subject2 = this.getAnotherInstanceOfSubject();
        Assertions.assertFalse(subject1.hashCode()==subject2.hashCode()&&subject2.hashCode()==subject1.hashCode());
    }

    @Test
    void EqualIsSymmetric2(){
        Subject subject1 = this.getInstanceOfSubject();
        Subject subject2 = this.getAnotherInstanceOfSubject();
        Assertions.assertFalse(subject1.equals(subject2)&&subject2.equals(subject1));
    }

    @Test
    void EqualIsTransitive(){
        Subject subject1 = this.getInstanceOfSubject();
        Subject subject2 = this.getInstanceOfSubject();
        Subject subject3 = this.getInstanceOfSubject();
        Assertions.assertTrue(subject1.equals(subject2)&&subject2.equals(subject3)&&subject1.equals(subject3));
    }
    @Test
    void EqualIsTransitive2(){
        Subject subject1 = this.getInstanceOfSubject();
        Subject subject2 = this.getAnotherInstanceOfSubject();
        Subject subject3 = this.getInstanceOfSubject();
        Assertions.assertFalse(subject1.equals(subject2)&&subject2.equals(subject3)&&subject1.equals(subject3));
    }

    @Test
    void HashCodeIsTransitive(){
        Subject subject1 = this.getInstanceOfSubject();
        Subject subject2 = this.getInstanceOfSubject();
        Subject subject3 = this.getInstanceOfSubject();
        Assertions.assertTrue(subject1.hashCode()==subject2.hashCode()&&
                subject2.hashCode()==subject3.hashCode()&&
                subject1.hashCode()==subject3.hashCode());
    }

    @Test
    void HashCodeIsTransitive2(){
        Subject subject1 = this.getInstanceOfSubject();
        Subject subject2 = this.getAnotherInstanceOfSubject();
        Subject subject3 = this.getInstanceOfSubject();
        Assertions.assertFalse(subject1.hashCode()==subject2.hashCode()&&
                subject2.hashCode()==subject3.hashCode()&&
                subject1.hashCode()==subject3.hashCode());
    }

    @Test
    void ShoudlReturnToSTringOfSubject(){
        Assertions.assertEquals("Name",
                getInstanceOfSubject().toString());
    }

    @Test
    void NullEquality(){
        Subject subject1 = this.getInstanceOfSubject();
        Assertions.assertNotEquals(null,subject1);
    }
}
