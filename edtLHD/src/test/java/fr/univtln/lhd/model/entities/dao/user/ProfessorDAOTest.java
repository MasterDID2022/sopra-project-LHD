package fr.univtln.lhd.model.entities.dao.user;

import fr.univtln.lhd.exception.IdException;
import fr.univtln.lhd.model.entities.user.Professor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Disabled
class ProfessorDAOTest {

    public ProfessorDAO getDAO() {
        ProfessorDAO st = null;
        try {
            st = new ProfessorDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return st;
    }

    private Professor getRandomNewProfessor(){
        Professor professor = Professor.of("UnitTest","UnitTestFirstName",
                "UnitTestName.Firstname"+Math.random()+"@email.com","REseracher");//A professor is a new one if is email is new
        return professor;
    }

    
    private Professor getTheTestProfessor(){
        Professor professor = Professor.of("TheTestProfessor","UnitTestFirstName",
                "UnitTestName.Firstname@email.com","Researcher");
        return professor;
    }

    @Test
    void CreateADAO(){
        ProfessorDAO dao = getDAO();
        Assertions.assertNotNull(dao);
    }

    @Test
    void addNewProfessor(){
        ProfessorDAO dao = getDAO();
        Professor professor = getRandomNewProfessor();
        int oldsize = dao.getAll().size();
        dao.save(professor,"1234");
        assertEquals(oldsize+1,dao.getAll().size());
        dao.delete(professor);
    }

    @Test
    void updateAprofessor() throws IdException {
        ProfessorDAO dao = getDAO();
        Professor professor = getRandomNewProfessor();
        Professor professor1 = Professor.of(professor.getName()+"1",professor.getFname()+"1",professor.getEmail()+"1", professor.getTitle());
        dao.save(professor,"1234");
        professor1.setId(professor.getId());
        dao.update(professor1);
        assertEquals(dao.get(professor.getId()).get(),professor1);
    }

    @Test
    void addSameProfessor(){
        ProfessorDAO dao = getDAO();
        Professor professor = getTheTestProfessor();
        dao.save(professor,"1234");
        int oldsize = dao.getAll().size();
        dao.save(professor,"1234");
        assertEquals(oldsize,dao.getAll().size());
    }


    @Test
    void deleteTheProfessor(){
        ProfessorDAO dao = getDAO();
        Professor professor = getRandomNewProfessor();
        dao.save(professor,"1234");
        int oldsize = dao.getAll().size();
        dao.delete(professor);
        assertEquals(oldsize-1,dao.getAll().size());
    }

}