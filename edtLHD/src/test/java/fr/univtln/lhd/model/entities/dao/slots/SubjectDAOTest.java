package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.slots.Subject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubjectDAOTest {

    public SubjectDAO getDAO() {
        SubjectDAO st = null;
            st = SubjectDAO.getInstance();

        return st;
    }


    private Subject getSubject(){//no cons on unicity random is useless
        Subject subject = Subject.getInstance("Matiere A"+Math.random(),150);
        return subject;
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
        Optional<Subject> subject = dao.get(1);
        System.out.println("tt="+subject.get());
        Subject subject1 = Subject.getInstance(subject.get().getName()+"1",subject.get().getHourCountMax()+1);
        subject1.setId(1);
        System.out.println(subject1);
        dao.update(subject1);
        assertEquals(subject1,dao.get(subject.get().getId()).get());
    }
}