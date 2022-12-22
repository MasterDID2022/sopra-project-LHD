package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.slots.Classroom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ClassroomDAOTest {

    public ClassroomDAO getDAO() {
        return ClassroomDAO.getInstance();
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
        try {
            int oldSize = dao.getAll().size();
            dao.save(classroom);
            assertEquals(oldSize+1, dao.getAll().size());
            dao.delete(classroom);
        } catch (SQLException e){
            throw new RuntimeException();
        }
    }

    @Test
    void updateClassroom() {
        ClassroomDAO dao = getDAO();
        Classroom classroom = getRandomNewClassroom();
        Classroom classroom1 = Classroom.getInstance(classroom.getName()+"1");

        try {
            dao.save(classroom);
            classroom1.setId(classroom.getId());
            dao.update(classroom1);
            assertEquals(dao.get(classroom.getId()).get(), classroom1);
        } catch (SQLException | IdException e){
            throw new RuntimeException();
        }
    }

    @Test
    void addSameClassroom(){
        ClassroomDAO dao = getDAO();
        Classroom classroom = getTheTestClassroom();
        final String defaultMsg = "Done Save Without Error";

        SQLException thrown = assertThrows(
                SQLException.class,
                () -> dao.save(classroom),
                defaultMsg
        );

        assertFalse(thrown.getMessage().contentEquals(defaultMsg));
    }

    @Test
    void deleteClassroom(){
        ClassroomDAO dao = getDAO();
        Classroom classroom = getRandomNewClassroom();

        try {
            dao.save(classroom);
            int oldSize = dao.getAll().size();
            dao.delete(classroom);
            assertEquals(oldSize-1,dao.getAll().size());
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }
}
