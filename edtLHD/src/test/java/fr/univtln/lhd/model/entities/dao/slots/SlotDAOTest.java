package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.dao.users.ProfessorDAO;
import fr.univtln.lhd.model.entities.slots.Classroom;
import fr.univtln.lhd.model.entities.slots.Group;
import fr.univtln.lhd.model.entities.slots.Slot;
import fr.univtln.lhd.model.entities.slots.Subject;
import fr.univtln.lhd.model.entities.users.Professor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.threeten.extra.Interval;

import java.sql.SQLException;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SlotDAOTest {

    private Classroom classroom;
    private Subject subject;

    private Professor professor1, professor2;
    private Group group1, group2;

    public SlotDAO getDAO() {
        return SlotDAO.getInstance();
    }

    @BeforeEach
    public void initializeTestEnvironment() {
        ClassroomDAO classroomDAO = ClassroomDAO.getInstance();
        SubjectDAO subjectDAO = SubjectDAO.getInstance();

        try {
            classroom = Classroom.getInstance("ClassroomTestOnly");
            classroomDAO.save(classroom);

            subject = Subject.getInstance("SubjectTestOnly", 100);
            subjectDAO.save(subject);
        } catch (SQLException e) {
            throw new AssertionError();
        }
    }

    private void initializeFullTestEnvironment() {
        ProfessorDAO professorDAO = ProfessorDAO.of();
        GroupDAO groupDAO = GroupDAO.getInstance();
        try {
            professor1 = Professor.of("nameP1", "fnameP2", "mail@mail.com", "titleP1");
            professor2 = Professor.of("nameP2", "fnameP2", "mail2@mail.com", "titleP2");
            professorDAO.save(professor1, "PasswordTestOnly");
            professorDAO.save(professor2, "PasswordTestOnly");

            group1 = Group.getInstance("Name1");
            group2 = Group.getInstance("Name2");
            groupDAO.save(group1);
            groupDAO.save(group2);
        } catch (SQLException e) {
            throw new AssertionError();
        }
    }

    @AfterEach
    public void deleteTestEnvironment() {
        try {
            ClassroomDAO.getInstance().delete(classroom);
            SubjectDAO.getInstance().delete(subject);
        } catch (SQLException e) {
            throw new AssertionError();
        }
    }

    private void deleteFullTestEnvironment() {
        ProfessorDAO professorDAO = ProfessorDAO.of();
        GroupDAO groupDAO = GroupDAO.getInstance();
        try {
            professorDAO.delete(professor1);
            professorDAO.delete(professor2);

            groupDAO.delete(group1);
            groupDAO.delete(group2);
        } catch (SQLException e) {
            throw new AssertionError();
        }
    }

    private Slot getRandomNewSlot() {
        return Slot.getInstance(
                Slot.SlotType.CM,
                classroom,
                subject,
                new HashSet<>(),
                new HashSet<>(),
                Interval.of(Instant.ofEpochSecond((long) (1 + Math.random())), Instant.ofEpochSecond((long) (100 + Math.random())))
        );
    }

    private Slot getRandomFullNewSlot() {
        return Slot.getInstance(
                Slot.SlotType.TP,
                classroom,
                subject,
                Set.of(group1, group2),
                Set.of(professor1, professor2),
                Interval.of(Instant.ofEpochSecond((long) (10000 + Math.random())), Instant.ofEpochSecond((long) (1000000 + Math.random())))
        );
    }

    private Slot getTestSlot() {
        try {
            Slot slot = getRandomNewSlot();
            getDAO().save(slot);
            return getDAO().get(slot.getId()).orElseThrow(SQLException::new);
        } catch (SQLException e) {
            throw new AssertionError();
        }
    }

    @Test
    void CreateDAO() {
        SlotDAO dao = getDAO();
        Assertions.assertNotNull(dao);
    }


    @Test
    void GetSlotFromGroup() {
        initializeFullTestEnvironment();
        SlotDAO slotDAO = getDAO();
        GroupDAO groupDAO = GroupDAO.getInstance();
        Slot slot = getRandomFullNewSlot();
        try {
            slotDAO.save(slot);

            Group firstGroup = groupDAO.get(slot.getGroup().iterator().next().getId()).orElseThrow(SQLException::new);
            List<Slot> groupSlots = slotDAO.getSlotOfGroup(firstGroup);
            assertEquals(groupSlots.get(0), slot);
        } catch (SQLException e) {
            throw new AssertionError();
        }

        deleteFullTestEnvironment();
    }

    @Test
    void saveNewSlot() {
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
        initializeFullTestEnvironment();

        SlotDAO slotDAO = SlotDAO.getInstance();
        GroupDAO groupDAO = GroupDAO.getInstance();
        ProfessorDAO professorDAO = ProfessorDAO.of();
        Slot slot = getRandomFullNewSlot();

        try {
            int oldGroupSlotJTSize = groupDAO.getAllGroupSlot().size();
            int oldProfessorSlotJTSize = professorDAO.getAllProfessorSlot().size();

            slotDAO.save(slot);

            int newGroupSlotJTSize = groupDAO.getAllGroupSlot().size();
            int newProfessorSlotJTSize = professorDAO.getAllProfessorSlot().size();

            assertEquals(oldGroupSlotJTSize + slot.getGroup().size(), newGroupSlotJTSize);
            assertEquals(oldProfessorSlotJTSize + slot.getProfessors().size(), newProfessorSlotJTSize);

            slotDAO.delete(slot);
        } catch (SQLException e) {
            throw new AssertionError();
        }

        deleteFullTestEnvironment();
    }

    @Test
    void testJoinTablesErrorWithoutSaving() {
        professor1 = Professor.of("nameP1", "fnameP2", "noSaveMail@mail.com", "titleP1");
        professor2 = Professor.of("nameP2", "fnameP2", "noSaveMail2@mail.com", "titleP2");
        group1 = Group.getInstance("NoSaveName1");
        group2 = Group.getInstance("NoSaveName2");

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
        } catch (SQLException e) {
            throw new AssertionError();
        }

        assertFalse(thrown.getMessage().contentEquals(defaultMsg));
    }


    @Test
    void GetSlotTest() {
        Slot s = getTestSlot();
        assertNotNull(s);
    }

    @Test
    void updateSlot() {
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
    void addSameSlot() {
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
    void deleteSlot() {
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
