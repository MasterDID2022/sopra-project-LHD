package fr.univtln.lhd.entities.user;

import fr.univtln.lhd.model.entities.users.Professor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorTest {

    public Professor getInstanceOfLecturer(){
        return Professor.of("Name","FirstName","Name.Firstname@emal.com","chercheur");
    }

    @Test
     void shouldReturnNotNull(){
        Professor professor = this.getInstanceOfLecturer();
        assertNotNull(professor);
    }

    @Test
    void shouldGetTitle(){
        Professor professor = this.getInstanceOfLecturer();
        assertEquals("chercheur", professor.getTitle());
    }

}