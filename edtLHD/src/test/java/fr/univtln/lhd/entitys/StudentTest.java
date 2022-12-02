package fr.univtln.lhd.entitys;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    @Test
     void shouldReturnNotNull(){
        Student student = Student.of("Name","FirstName","Name.Firstname@emal.com");
        assertNotNull(student);
    }


}