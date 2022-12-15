package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.exception.IdException;
import fr.univtln.lhd.model.entities.dao.user.ProfessorDAO;
import fr.univtln.lhd.model.entities.slots.Subject;
import fr.univtln.lhd.model.entities.user.Admin;
import fr.univtln.lhd.model.entities.user.Professor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubjectDAOTest {

    public SubjectDAO getDAO() {
        SubjectDAO st = null;
        try {
            st = new SubjectDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        int oldsize = dao.getAll().size();
        dao.save(subject);
        assertEquals(oldsize+1,dao.getAll().size());
    }


    @Test
    void updateASubject() throws IdException {
        SubjectDAO dao = getDAO();
        Optional<Subject> subject = dao.get(2);
        System.out.println("tt="+subject.get());
        Subject subject1 = Subject.getInstance(subject.get().getName()+"1",subject.get().getHourCountMax()+1);
        subject1.setId(2);
        System.out.println(subject1);
        dao.update(subject1);
        assertEquals(subject1,dao.get(subject.get().getId()).get());
    }
}