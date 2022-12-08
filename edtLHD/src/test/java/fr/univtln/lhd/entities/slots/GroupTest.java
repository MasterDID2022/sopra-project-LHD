package fr.univtln.lhd.entities.slots;

import static org.junit.jupiter.api.Assertions.*;

import fr.univtln.lhd.model.entities.slots.Group;
import fr.univtln.lhd.model.entities.user.Student;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class GroupTest {


    public List<Student> getListOfStudents(){
        List<Student> students = new ArrayList<>();
        return students;
    }

    public Group getInstanceOfGroup() {
        return Group.getInstance("Name", getListOfStudents());
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
    void testStudentNotNull(){
        Group group = getInstanceOfGroup();
        assertNotNull(group.getStudents());
    }

    @Test
    void Addtest(){
        Group group = getInstanceOfGroup();
        group.add(Student.of("name","fname","email"));
        assertEquals(1, group.getStudents().size());
    }

    @Test
    void testStudentCountEquality0(){
        Group group = getInstanceOfGroup();
        assertEquals(0, group.getStudents().size());
    }

    @Test
    void testStudentCountEquality1(){
        Group group = getInstanceOfGroup();
        group.add(Student.of("name","fname","email"));
        assertEquals(1, group.getStudents().size());
    }
}