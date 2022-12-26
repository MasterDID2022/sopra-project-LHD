package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.slots.Classroom;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ClassroomDAOTest {

    private Classroom classroom;

    public ClassroomDAO getDAO() {
        return ClassroomDAO.getInstance();
    }

    private Classroom getRandomNewClassroom(){
        return Classroom.getInstance("Random"+Math.random());
    }

    @BeforeEach
    public void initializeTestEnvironment() {
        classroom = Classroom.getInstance("ClassTestingOnly");

        try {
            getDAO().save(classroom);
        } catch (SQLException e){
            throw new AssertionError();
        }
    }

    @AfterEach
    public void deleteTestEnvironment() {
        try {
            getDAO().delete(classroom);
        } catch (SQLException e){
            throw new AssertionError();
        }
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
        Classroom classroom1 = Classroom.getInstance(classroom.getName()+"1");

        try {
            classroom1.setId(classroom.getId());
            dao.update(classroom1);
            assertEquals(dao.get(classroom.getId()).orElseThrow(SQLException::new), classroom1);
        } catch (SQLException | IdException e){
            throw new RuntimeException();
        }
    }

    @Test
    void addSameClassroom(){
        ClassroomDAO dao = getDAO();
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
