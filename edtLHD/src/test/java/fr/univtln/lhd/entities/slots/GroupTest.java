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
        Student s = Student.of("Name", "FName", "Mail");
        students.add(s);
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
    void testStudentCountEquality(){
        Group group = getInstanceOfGroup();
        assertEquals(1, group.getStudents().size());
    }
}