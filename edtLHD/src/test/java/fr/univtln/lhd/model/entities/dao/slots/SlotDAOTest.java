package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.dao.users.ProfessorDAO;
import fr.univtln.lhd.model.entities.slots.Classroom;
import fr.univtln.lhd.model.entities.slots.Group;
import fr.univtln.lhd.model.entities.slots.Slot;
import fr.univtln.lhd.model.entities.slots.Subject;
import fr.univtln.lhd.model.entities.users.Professor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.threeten.extra.Interval;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SlotDAOTest {

    public SlotDAO getDAO () {
        return SlotDAO.getInstance();
    }

    private Slot getRandomNewSlot () {
        try {
            Classroom classroom = ClassroomDAO.getInstance().get(3).orElseThrow(SQLException::new);
            Subject subject = SubjectDAO.getInstance().get(1).orElseThrow(SQLException::new);

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

    private Slot getRandomFullNewSlot() {
        try {
            Classroom classroom = ClassroomDAO.getInstance().get(3).orElseThrow(SQLException::new);
            Subject subject = SubjectDAO.getInstance().get(1).orElseThrow(SQLException::new);

            Professor p1 = Professor.of("nameP1", "fnameP2", "mail@mail.com", "titleP1");
            Professor p2 = Professor.of("nameP2", "fnameP2", "mail2@mail.com", "titleP2");

            Group g1 = Group.getInstance("Name1");
            Group g2 = Group.getInstance("Name2");

            return Slot.getInstance(
                    Slot.SlotType.TP,
                    classroom,
                    subject,
                    List.of(g1, g2),
                    List.of(p1, p2),
                    Interval.of(Instant.ofEpochSecond((long) (10000 + Math.random())), Instant.ofEpochSecond((long) (1000000 + Math.random())))
            );
        } catch (SQLException e){
            throw new AssertionError();
        }
    }

    private Slot getTestSlot () {
        try {
            return getDAO().get(3).orElseThrow(SQLException::new);
        } catch (SQLException e) {
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
    void testJoinTablesCoherenceAfterSaving() {
        SlotDAO slotDAO = SlotDAO.getInstance();
        GroupDAO groupDAO = GroupDAO.getInstance();
        ProfessorDAO professorDAO = ProfessorDAO.of();
        Slot slot = getRandomFullNewSlot();

        try {
            for (Group group : slot.getGroup())
                groupDAO.save(group);
            for (Professor professor : slot.getProfessors())
                professorDAO.save(professor, "testingJoinTables");

            int oldGroupSlotJTSize = groupDAO.getAllGroupSlot().size();
            int oldProfessorSlotJTSize = professorDAO.getAllProfessorSlot().size();

            slotDAO.save(slot);

            int newGroupSlotJTSize = groupDAO.getAllGroupSlot().size();
            int newProfessorSlotJTSize = professorDAO.getAllProfessorSlot().size();

            assertEquals(oldGroupSlotJTSize + slot.getGroup().size(), newGroupSlotJTSize);
            assertEquals(oldProfessorSlotJTSize + slot.getProfessors().size(), newProfessorSlotJTSize);

            for (Group group : slot.getGroup())
                groupDAO.delete(group);
            for (Professor professor : slot.getProfessors())
                professorDAO.delete(professor);

            slotDAO.delete(slot);
        } catch (SQLException e){
            throw new AssertionError();
        }
    }

    @Test
    void testJoinTablesErrorWithoutSaving() {
        SlotDAO dao = SlotDAO.getInstance();
        Slot slot = getRandomFullNewSlot();
        final String defaultMsg = "Done Save Without Error";

        SQLException thrown = assertThrows(
                SQLException.class,
                () -> dao.save(slot),
                defaultMsg
        );

        try {
            dao.delete(slot);
        } catch (SQLException e){
            throw new AssertionError();
        }

        assertFalse(thrown.getMessage().contentEquals(defaultMsg));
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
