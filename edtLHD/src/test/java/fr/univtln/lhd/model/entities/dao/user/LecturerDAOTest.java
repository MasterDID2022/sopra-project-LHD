package fr.univtln.lhd.model.entities.dao.user;

import fr.univtln.lhd.exception.IdException;
import fr.univtln.lhd.model.entities.dao.Datasource;
import fr.univtln.lhd.model.entities.user.Lecturer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
class LecturerDAOTest {
    public LecturerDAO getDAO() {
        try (
            Connection conn = Datasource.getConnection()) {
            LecturerDAO st = new LecturerDAO();
            return st;
        } catch ( SQLException e){
            log.error(e.getMessage());
        }
        return null;
    }

    private Lecturer getRandomNewLecturer(){
        Lecturer lecturer = Lecturer.of("UnitTest","UnitTestFirstName",
                "UnitTestName.Firstname"+Math.random()+"@email.com","professor");//A lecturer is a new one if is email is new
        return lecturer;
    }

    
    private Lecturer getTheTestLecturer(){
        Lecturer lecturer = Lecturer.of("UnitTest","UnitTestFirstName",
                "UnitTestName.Firstname@email.com","professor");
        return lecturer;
    }

    @Test
    void addNewLecturer(){
        LecturerDAO dao = getDAO();
        Lecturer lecturer = getRandomNewLecturer();
        int oldsize = dao.getAll().size();
        dao.save(lecturer,"1234");
        assertEquals(oldsize+1,dao.getAll().size());
    }

    @Test
    void addSameLecturer(){
        LecturerDAO dao = getDAO();
        Lecturer lecturer = getTheTestLecturer();
        dao.save(lecturer,"1234");
        int oldsize = dao.getAll().size();
        dao.save(lecturer,"1234");
        assertEquals(oldsize,dao.getAll().size());
    }

    @Test
    void upddateTheLecturerAndTheGetter() throws IdException {
        LecturerDAO dao = getDAO();
        Lecturer lecturer = getTheTestLecturer();
        Optional<Lecturer> lecturerFromBD;
        dao.save(lecturer,"1234");
        Map param = new HashMap<>();
        param.put("name","nouveaux");
        param.put("fname","fnouveaux");
        param.put("email","nv@fnouveaux.fr");
        lecturer=dao.updateAndGet(lecturer,param);
        lecturerFromBD = dao.get(lecturer.getId());
        assertEquals(lecturerFromBD.get().getName(),lecturer.getName());
    }

    @Test
    void upddateTheLecturer() throws IdException {
        LecturerDAO dao = getDAO();
        Lecturer lecturer = getTheTestLecturer();
        Optional<Lecturer> lecturerFromBD;
        dao.save(lecturer,"1234");
        Map param = new HashMap<>();
        param.put("name","nouveaux");
        param.put("fname","fnouveaux");
        param.put("email","nv@fnouveaux.fr"+Math.random());
        dao.update(lecturer,param);
        lecturerFromBD = dao.get(lecturer.getId());
        assertNotEquals(lecturerFromBD.get().getName(),lecturer.getName());
    }

    @Test
    void deleteTheLecturer(){
        LecturerDAO dao = getDAO();
        Lecturer lecturer = getTheTestLecturer();
        dao.save(lecturer,"1234");
        int oldsize = dao.getAll().size();
        dao.delete(lecturer);
        assertEquals(oldsize-1,dao.getAll().size());
    }

}