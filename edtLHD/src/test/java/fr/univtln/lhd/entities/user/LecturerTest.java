package fr.univtln.lhd.entities.user;

import fr.univtln.lhd.model.entities.users.Lecturer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LecturerTest {

    public Lecturer getInstanceOfLecturer(){
        return Lecturer.of("Name","FirstName","Name.Firstname@emal.com","chercheur");
    }

    @Test
     void shouldReturnNotNull(){
        Lecturer lecturer = this.getInstanceOfLecturer();
        assertNotNull(lecturer);
    }

    @Test
    void shouldGetTitle(){
        Lecturer lecturer = this.getInstanceOfLecturer();
        assertEquals("chercheur",lecturer.getTitle());
    }

}