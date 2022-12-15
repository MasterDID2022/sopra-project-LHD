package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.slots.Classroom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassroomDAOTest {

    public ClassroomDAO getDAO() {
        ClassroomDAO classroomDAO = null;
        try {
            classroomDAO = ClassroomDAO.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return classroomDAO;
    }

    private Classroom getRandomNewClassroom(){
        return Classroom.getInstance("Random"+Math.random());
    }

    private Classroom getTheTestClassroom(){
        return Classroom.getInstance("TestClassroom");
    }

    @Test
    void CreateDAO(){
        ClassroomDAO dao = getDAO();
        Assertions.assertNotNull(dao);
    }

    @Test
    void saveNewClassroom(){
        ClassroomDAO dao = getDAO();
        Classroom classroom = getRandomNewClassroom();
        int oldsize = dao.getAll().size();
        dao.save(classroom);
        assertEquals(oldsize+1,dao.getAll().size());
        dao.delete(classroom);
    }

    @Test
    void updateClassroom() throws IdException {
        ClassroomDAO dao = getDAO();
        Classroom classroom = getRandomNewClassroom();
        Classroom classroom1 = Classroom.getInstance(classroom.getName()+"1");
        dao.save(classroom);
        classroom1.setId(classroom.getId());
        dao.update(classroom1);
        assertEquals(dao.get(classroom.getId()).get(),classroom1);
    }

    @Test
    void addSameClassroom(){
        ClassroomDAO dao = getDAO();
        Classroom classroom = getTheTestClassroom();
        dao.save(classroom);
        int oldsize = dao.getAll().size();
        dao.save(classroom);
        assertEquals(oldsize,dao.getAll().size());
    }

    @Test
    void deleteClassroom(){
        ClassroomDAO dao = getDAO();
        Classroom classroom = getRandomNewClassroom();
        dao.save(classroom);
        int oldsize = dao.getAll().size();
        dao.delete(classroom);
        assertEquals(oldsize-1,dao.getAll().size());
    }
}
