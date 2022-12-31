package fr.univtln.lhd.model.entities.user;

import fr.univtln.lhd.model.entities.users.Professor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfessorTest {

    public Professor getInstanceOfProfessor(){
        return Professor.of("Name","FirstName","Name.Firstname@emal.com","chercheur");
    }

    public Professor getAnotherInstanceOfProfessor(){
        return Professor.of("NameAlternative","FirstNameAlternative","Name.Firaleternatiestname@emal.com","chercheur");
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

    @Test
    void HashCodeIsReflexive(){
        Professor professor = this.getInstanceOfProfessor();
        Assertions.assertEquals(professor.hashCode(),professor.hashCode());
    }

    @Test
    void HashCodeIsSymmetric(){
        Professor professor1 = this.getInstanceOfProfessor();
        Professor professor2 = this.getInstanceOfProfessor();
        Assertions.assertTrue(professor1.hashCode()==professor2.hashCode()&&professor2.hashCode()==professor1.hashCode());
    }

    @Test
    void HashCodeIsSymmetric2(){
        Professor professor1 = this.getInstanceOfProfessor();
        Professor professor2 = this.getAnotherInstanceOfProfessor();
        Assertions.assertFalse(professor1.hashCode()==professor2.hashCode()&&professor2.hashCode()==professor1.hashCode());
    }



    @Test
    void HashCodeIsTransitive(){
        Professor professor1 = this.getInstanceOfProfessor();
        Professor professor2 = this.getInstanceOfProfessor();
        Professor professor3 = this.getInstanceOfProfessor();
        Assertions.assertTrue(professor1.hashCode()==professor2.hashCode()&&
                professor2.hashCode()==professor3.hashCode()&&
                professor1.hashCode()==professor3.hashCode());
    }

    @Test
    void HashCodeIsTransitive2(){
        Professor professor1 = this.getInstanceOfProfessor();
        Professor professor2 = this.getAnotherInstanceOfProfessor();
        Professor professor3 = this.getInstanceOfProfessor();
        Assertions.assertFalse(professor1.hashCode()==professor2.hashCode()&&
                professor2.hashCode()==professor3.hashCode()&&
                professor1.hashCode()==professor3.hashCode());
    }

    @Test
    void ShouldDisplayTheName(){
       Professor professor = getInstanceOfProfessor();
       Assertions.assertEquals("NAME F.",professor.getDisplayName());
    }


}