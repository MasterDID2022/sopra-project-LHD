package fr.univtln.lhd.entities.user;

import fr.univtln.lhd.model.entities.users.Professor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorTest {

    public Professor getInstanceOfProfessor(){
        return Professor.of("Name","FirstName","Name.Firstname@emal.com","chercheur");
    }

    @Test
     void shouldReturnNotNull(){
        Professor professor = this.getInstanceOfProfessor();
        assertNotNull(professor);
    }

    @Test
    void shouldGetTitle(){
        Professor professor = this.getInstanceOfProfessor();
        assertEquals("chercheur", professor.getTitle());
    }

}