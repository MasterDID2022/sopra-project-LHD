package fr.univtln.lhd.model.entities.dao.users;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.users.Professor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class ProfessorDAOTest {
    public static final ProfessorDAO dao = ProfessorDAO.of();

    private Professor getRandomNewProfessor(){
        return Professor.of("UnitTest","UnitTestFirstName",
                "UnitTestName.Firstname"+Math.random()+"@email.com","REseracher");
    }

    
    private Professor getTheTestProfessor(){
        return Professor.of("TheTestProfessor","UnitTestFirstName",
                "UnitTestName.Firstname@email.com","Researcher");
    }

    @Test
    void CreateADAO(){
        Assertions.assertNotNull(dao);
    }

    @Test
    void addNewProfessor(){
        Professor professor = getRandomNewProfessor();
        int oldsize = dao.getAll().size();
        dao.save(professor,"1234");
        assertEquals(oldsize+1,dao.getAll().size());
        dao.delete(professor);
    }

    @Test
    void getProfessorFromAuthTest(){
        Professor professor = getTheTestProfessor();

        try {
            Professor authGetterProfessor = dao.get(professor.getEmail(), "1234").orElse(null);
            assertEquals(professor, authGetterProfessor);
        }catch (SQLException e){
            log.error(e.getMessage());
            throw new AssertionError();
        }
    }

    @Test
    void updateAprofessor() throws IdException {
        Professor professor = getRandomNewProfessor();
        Professor professor1 = Professor.of(professor.getName()+"1",professor.getFname()+"1",professor.getEmail()+"1", professor.getTitle());
        dao.save(professor,"1234");
        professor1.setId(professor.getId());
        dao.update(professor1);
        assertEquals(dao.get(professor.getId()).orElseThrow(AssertionError::new),professor1);
    }

    @Test//Not realy a test must be change when slot is implemented TODO
    void getProfessorOfASlot(){
        System.out.println(dao.getProfessorOfSlots(3));
        assertEquals(2,1+1);
    }

    @Test
    void addSameProfessor(){
        Professor professor = getTheTestProfessor();
        dao.save(professor,"1234");
        int oldsize = dao.getAll().size();
        dao.save(professor,"1234");
        assertEquals(oldsize,dao.getAll().size());
    }


    @Test
    void deleteTheProfessor(){
        Professor professor = getRandomNewProfessor();
        dao.save(professor,"1234");
        int oldsize = dao.getAll().size();
        dao.delete(professor);
        assertEquals(oldsize-1,dao.getAll().size());
    }

}