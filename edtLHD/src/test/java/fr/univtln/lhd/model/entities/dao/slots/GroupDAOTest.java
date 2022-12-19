package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.slots.Group;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GroupDAOTest {

    public GroupDAO getDAO() {
        GroupDAO st = null;
        try {
            st = GroupDAO.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return st;
    }


    private Group getGroup(){//no cons on unicity random is useless
        return Group.getInstance("Matiere A"+Math.random());
    }

    @Test
    void CreateDao(){
        Assertions.assertNotNull(getDAO());
    }

    @Test
    void SaveGroup(){
        GroupDAO dao = getDAO();
        Group group = getGroup();
        int oldsize = dao.getAll().size();
        dao.save(group);
        assertEquals(oldsize+1,dao.getAll().size());
    }


    @Test
    void updateAGroup() throws IdException {
        GroupDAO dao = getDAO();
        Optional<Group> group = dao.get(2);
        System.out.println("tt="+group.orElse(null));
        Group group1 = Group.getInstance(group.orElseThrow().getName()+"1");
        group1.setId(2);
        System.out.println(group1);
        dao.update(group1);
        assertEquals(group1,dao.get(group.get().getId()).orElse(null));
    }

}