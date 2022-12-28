package fr.univtln.lhd.model.entities.user;

import fr.univtln.lhd.model.entities.slots.Group;
import fr.univtln.lhd.model.entities.users.Professor;
import fr.univtln.lhd.model.entities.users.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {


    public Student getInstanceOfStudent(){
        return Student.of("Name","FirstName","Name.Firstname@emal.com");
    }

    public Student getAnotherInstanceOfStudent(){
        return Student.of("NameAlternative","FirstNameAlternative","Name.Firaleternatiestname@emal.com");
    }
    @Test
     void shouldReturnNotNullWhithoutGroup(){
        Student student = Student.of("Name","FirstName","Name.Firstname@emal.com");
        assertNotNull(student);
    }


    @Test
    void shouldReturnNotNullWhithGroup(){
        List<Group> groups = new ArrayList<>();
        groups.add(Group.getInstance("GroupTest"));
        Student student = Student.of("Name","FirstName","Name.Firstname@emal.com",groups);
        assertNotNull(student);
    }

    @Test
    void addToEmptyGroup(){
        Student student = Student.of("Name","FirstName","Name.Firstname@emal.com");
        student.add(Group.getInstance("groupe"));
        assertEquals(1,student.getStudentGroup().size());
    }

    @Test
    void addToGroup(){
        Student student = Student.of("Name","FirstName","Name.Firstname@emal.com");
        student.add(Group.getInstance("groupe2"));
        student.add(Group.getInstance("groupe"));
        assertEquals(2,student.getStudentGroup().size());
    }
    @Test
    void HashCodeIsReflexive(){
        Student student = this.getInstanceOfStudent();
        Assertions.assertEquals(student.hashCode(),student.hashCode());
    }

    @Test
    void HashCodeIsSymmetric(){
        Student student1 = this.getInstanceOfStudent();
        Student student2 = this.getInstanceOfStudent();
        Assertions.assertTrue(student1.hashCode()==student2.hashCode()&&student2.hashCode()==student1.hashCode());
    }

    @Test
    void HashCodeIsSymmetric2(){
        Student student1 = this.getInstanceOfStudent();
        Student student2 = this.getAnotherInstanceOfStudent();
        Assertions.assertFalse(student1.hashCode()==student2.hashCode()&&student2.hashCode()==student1.hashCode());
    }



    @Test
    void HashCodeIsTransitive(){
        Student student1 = this.getInstanceOfStudent();
        Student student2 = this.getInstanceOfStudent();
        Student student3 = this.getInstanceOfStudent();
        Assertions.assertTrue(student1.hashCode()==student2.hashCode()&&
                student2.hashCode()==student3.hashCode()&&
                student1.hashCode()==student3.hashCode());
    }

    @Test
    void HashCodeIsTransitive2(){
        Student student1 = this.getInstanceOfStudent();
        Student student2 = this.getAnotherInstanceOfStudent();
        Student student3 = this.getInstanceOfStudent();
        Assertions.assertFalse(student1.hashCode()==student2.hashCode()&&
                student2.hashCode()==student3.hashCode()&&
                student1.hashCode()==student3.hashCode());
    }

    @Test
    void ShouldDisplayTheStudent(){
        Student student = getInstanceOfStudent();
        Assertions.assertEquals("Name FirstName Name.Firstname@emal.com [-1] from null",
                student.toString());
    }



}