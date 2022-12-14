package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.model.entities.slots.Subject;
import fr.univtln.lhd.model.entities.user.Admin;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

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
        Subject subject = Subject.getInstance("Matiere A",150);
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

}