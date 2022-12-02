package fr.univtln.lhd.entitys;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LecturerTest {
    @Test
    public void shouldReturnNotNull(){
        Lecturer lecturer = Lecturer.of("Name","FirstName","Name.Firstname@emal.com","Chercheur");
        assertNotNull(lecturer);
    }
}