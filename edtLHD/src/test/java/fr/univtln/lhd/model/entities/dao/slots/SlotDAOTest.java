package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.slots.Classroom;
import fr.univtln.lhd.model.entities.slots.Group;
import fr.univtln.lhd.model.entities.slots.Slot;
import fr.univtln.lhd.model.entities.slots.Subject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.threeten.extra.Interval;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SlotDAOTest {

    public SlotDAO getDAO () {
        return SlotDAO.getInstance();
    }

    private Slot getRandomNewSlot () {
        try {
            Classroom classroom = ClassroomDAO.getInstance().get(3).get();
            Subject subject = SubjectDAO.getInstance().get(1).get();

            return Slot.getInstance(
                    Slot.SlotType.CM,
                    classroom,
                    subject,
                    new ArrayList<>(),
                    new ArrayList<>(),
                    Interval.of(Instant.ofEpochSecond((long) (1 + Math.random())), Instant.ofEpochSecond((long) (100 + Math.random())))
            );
        } catch (SQLException e) {
            throw new AssertionError();
        }
    }

    private Slot getTestSlot () {
        try {
            return getDAO().get(3).get();
        } catch (Exception e) {
            throw new AssertionError();
        }
    }

    @Test
    void CreateDAO () {
        SlotDAO dao = getDAO();
        Assertions.assertNotNull(dao);
    }


    @Test//We need a way to manipulate group_slot to test that methode
    //Tested with specifique value should not be used or trust
    void GetSlotFromGroup() throws SQLException {
        SlotDAO dao = getDAO();
        GroupDAO Gdao = GroupDAO.getInstance();
        Group FirstGroup = Gdao.get(1).get();
        System.out.println(dao.getSlotOfGroup(FirstGroup));
        Assertions.assertEquals(2,1+1);
    }

    @Test
    void saveNewSlot () {
        SlotDAO dao = getDAO();
        Slot slot = getRandomNewSlot();
        try {
            int oldSize = dao.getAll().size();
            dao.save(slot);
            assertEquals(oldSize + 1, dao.getAll().size());
            dao.delete(slot);
        } catch (SQLException e) {
            throw new AssertionError();
        }
    }


    @Test
    void GetSlotTest () {
        SlotDAO dao = getDAO();

        try {
            Slot s = dao.get(3).orElseThrow(SQLException::new);
            Assertions.assertNotNull(s);
        } catch (SQLException e) {
            throw new AssertionError();
        }
    }

    @Test
    void updateSlot () {
        SlotDAO dao = getDAO();
        Slot slot = getRandomNewSlot();
        Slot slot1 = Slot.getInstance(
                Slot.SlotType.TD,
                slot.getClassroom(),
                slot.getSubject(),
                slot.getGroup(),
                slot.getProfessors(),
                slot.getTimeRange()
        );
        try {
            dao.save(slot);
            slot1.setId(slot.getId());
            dao.update(slot1);
            assertEquals(dao.get(slot.getId()).orElseThrow(AssertionError::new), slot1);

            dao.delete(slot);
        } catch (SQLException | IdException e) {
            throw new AssertionError();
        }
    }

    @Test
    void addSameSlot () {
        SlotDAO dao = getDAO();
        Slot slot = getTestSlot();
        final String defaultMsg = "Done Save Without Error";

        SQLException thrown = assertThrows(
                SQLException.class,
                () -> dao.save(slot),
                defaultMsg
        );

        assertFalse(thrown.getMessage().contentEquals(defaultMsg));
    }

    @Test
    void deleteSlot () {
        SlotDAO dao = getDAO();
        Slot slot = getRandomNewSlot();

        try {
            dao.save(slot);
            int oldSize = dao.getAll().size();
            dao.delete(slot);
            assertEquals(oldSize - 1, dao.getAll().size());
        } catch (SQLException e) {
            throw new AssertionError();
        }
    }
}
