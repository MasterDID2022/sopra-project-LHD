package fr.univtln.lhd.model.entities.dao.user;

import fr.univtln.lhd.exception.IdException;
import fr.univtln.lhd.model.entities.dao.Datasource;
import fr.univtln.lhd.model.entities.user.Professor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Disabled
@Slf4j
class ProfessorDAOTest {

    public LecturerDAO getDAO() {
        try (
                Connection conn = Datasource.getInstance().getConnection()) {
            LecturerDAO st = new LecturerDAO();
            return st;
        } catch ( SQLException e){
            log.error(e.getMessage());
        }
        return null;
    }

    private Professor getRandomNewLecturer(){
        Professor professor = Professor.of("UnitTest","UnitTestFirstName",
                "UnitTestName.Firstname"+Math.random()+"@email.com","professor");//A lecturer is a new one if is email is new
        return professor;
    }


    private Professor getTheTestLecturer(){
        Professor professor = Professor.of("UnitTest","UnitTestFirstName",
                "UnitTestName.Firstname@email.com","professor");
        return professor;
    }

    @Test
    void addNewLecturer(){
        LecturerDAO dao = getDAO();
        Professor professor = getRandomNewLecturer();
        int oldsize = dao.getAll().size();
        dao.save(professor,"1234");
        assertEquals(oldsize+1,dao.getAll().size());
    }

    @Test
    void addSameLecturer(){
        LecturerDAO dao = getDAO();
        Professor professor = getTheTestLecturer();
        dao.save(professor,"1234");
        int oldsize = dao.getAll().size();
        dao.save(professor,"1234");
        assertEquals(oldsize,dao.getAll().size());
    }


    @Test
    void deleteTheLecturer(){
        LecturerDAO dao = getDAO();
        Professor professor = getTheTestLecturer();
        dao.save(professor,"1234");
        int oldsize = dao.getAll().size();
        dao.delete(professor);
        assertEquals(oldsize-1,dao.getAll().size());
    }

}