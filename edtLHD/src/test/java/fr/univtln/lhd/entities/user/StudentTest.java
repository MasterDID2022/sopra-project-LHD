package fr.univtln.lhd.entities.user;

import fr.univtln.lhd.model.entities.slots.Group;
import fr.univtln.lhd.model.entities.users.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
     void shouldReturnNotNullWhithoutGroup(){
        Student student = Student.of("Name","FirstName","Name.Firstname@emal.com");
        assertNotNull(student);
    }


    @Test
    void shouldReturnNotNullWhithGroup(){
        Student student = Student.of("Name","FirstName","Name.Firstname@emal.com");
        assertNotNull(student);
    }

    @Test
    void addToEmptyGroup(){
        Student student = Student.of("Name","FirstName","Name.Firstname@emal.com");
        student.add(Group.getInstance("groupe"));
        assertEquals(1,student.getStudendGroup().size());
    }

    @Test
    void addToGroup(){
        Student student = Student.of("Name","FirstName","Name.Firstname@emal.com");
        student.add(Group.getInstance("groupe2"));
        student.add(Group.getInstance("groupe"));
        assertEquals(2,student.getStudendGroup().size());
    }



}