package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.model.entities.slots.Group;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class GroupDAOTest {

    public GroupDAO getDAO() {
        return GroupDAO.getInstance();
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
        try {
            int oldSize = dao.getAll().size();
            dao.save(group);
            assertEquals(oldSize+1,dao.getAll().size());
        } catch (SQLException e){
            throw new RuntimeException();
        }
    }


    @Test
    void updateAGroup() {
        GroupDAO dao = getDAO();
        Group group = getGroup();
        Group group1 = Group.getInstance( group.getName() + "1" );

        try {
            dao.save(group);
            group1.setId(group.getId());
            dao.update(group1);
            assertEquals(dao.get(group.getId()).get(), group1);
        } catch (SQLException e){
            throw new RuntimeException();
        }
    }

}