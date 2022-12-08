package fr.univtln.lhd.entities.user;

import fr.univtln.lhd.model.entities.user.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
     void shouldReturnNotNull(){
        Student student = Student.of("Name","FirstName","Name.Firstname@emal.com");
        assertNotNull(student);
    }




}