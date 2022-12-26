package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.slots.Subject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubjectDAOTest {

    public SubjectDAO getDAO() {

        return SubjectDAO.getInstance();
    }


    private Subject getSubject(){ // no need for randomness here
        return Subject.getInstance("Matiere A"+Math.random(),150);
    }

    @Test
    void CreateDao(){
        Assertions.assertNotNull(getDAO());
    }

    @Test
    void SaveSubject(){
        SubjectDAO dao = getDAO();
        Subject subject = getSubject();
        int oldSize = dao.getAll().size();
        dao.save(subject);
        assertEquals(oldSize+1,dao.getAll().size());
        dao.delete(subject);
    }


    @Test
    void updateASubject() throws IdException {
        SubjectDAO dao = getDAO();
        Subject subject = getSubject();

        dao.save(subject);
        Subject subject1 = Subject.getInstance(subject.getName()+"1",subject.getHourCountMax()+1);
        subject1.setId(subject.getId());
        dao.update(subject1);

        Subject newUpdatedSubject = dao.get(subject.getId()).orElseThrow(AssertionError::new);
        assertEquals(subject1, newUpdatedSubject);

        dao.delete(subject);
    }
}