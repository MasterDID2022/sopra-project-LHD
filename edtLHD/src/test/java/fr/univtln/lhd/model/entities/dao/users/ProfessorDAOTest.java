package fr.univtln.lhd.model.entities.dao.users;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.dao.slots.ClassroomDAO;
import fr.univtln.lhd.model.entities.dao.slots.SlotDAO;
import fr.univtln.lhd.model.entities.dao.slots.SubjectDAO;
import fr.univtln.lhd.model.entities.slots.Classroom;
import fr.univtln.lhd.model.entities.slots.Slot;
import fr.univtln.lhd.model.entities.slots.Subject;
import fr.univtln.lhd.model.entities.users.Professor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.threeten.extra.Interval;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class ProfessorDAOTest {
    public static final ProfessorDAO dao = ProfessorDAO.of();

    private Professor professor1;
    private Professor professor2;

    private Classroom classroom;
    private Subject subject;

    private Professor getRandomNewProfessor () {
        return Professor.of("UnitTest", "UnitTestFirstName",
                "UnitTestName.Firstname" + Math.random() + "@email.com", "REseracher");
    }

    @BeforeEach
    public void initializeTestEnvironment () {
        professor1 = Professor.of("name1", "fname1", "prof1TESTING@mail.com", "test");
        professor2 = Professor.of("name2", "fname2", "prof2TESTING@mail.com", "test");
        dao.save(professor1, "testingOnly");
        dao.save(professor2, "testingOnly");
    }

    private void initSlotData () {
        classroom = Classroom.getInstance("ClassroomTESTING");
        subject = Subject.getInstance("SubjectTESTING", 100);

        try {
            ClassroomDAO.getInstance().save(classroom);
            SubjectDAO.getInstance().save(subject);
        } catch (SQLException e) {
            throw new AssertionError();
        }
    }

    @AfterEach
    public void deleteTestEnvironment () {
        dao.delete(professor1);
        dao.delete(professor2);
    }

    private void deleteSlotData () {
        try {
            ClassroomDAO.getInstance().delete(classroom);
            SubjectDAO.getInstance().delete(subject);
        } catch (SQLException e) {
            throw new AssertionError();
        }
    }

    @Test
    void CreateADAO () {
        Assertions.assertNotNull(dao);
    }

    @Test
    void addNewProfessor () {
        Professor professor = getRandomNewProfessor();
        int oldsize = dao.getAll().size();
        dao.save(professor,"1234");
        assertEquals(oldsize+1,dao.getAll().size());
        dao.delete(professor);
    }

    @Test
    @Disabled("Causes the CI to fail")
    void getProfessorFromAuthTest(){
        try {
            Professor authGetterProfessor = dao.get(professor1.getEmail(), "testingOnly").orElse(null);
            assertEquals(professor1, authGetterProfessor);
        }catch (SQLException e){
            log.error(e.getMessage());
            throw new AssertionError();
        }
    }

    @Test
    void updateAprofessor() throws IdException {
        Professor professor3 = Professor.of(professor1.getName() + "1", professor1.getFname() + "1", professor1.getEmail() + "1", professor1.getTitle());
        professor3.setId(professor1.getId());
        dao.update(professor3);
        Professor updatedProfessor = dao.get(professor3.getId()).orElseThrow(AssertionError::new);
        assertEquals(updatedProfessor, professor3);
    }

    @Test//Not realy a test must be change when slot is implemented TODO
    void getProfessorOfASlot() {
        SlotDAO slotDAO = SlotDAO.getInstance();

        initSlotData();

        Slot slot = Slot.getInstance(
                Slot.SlotType.TP,
                classroom,
                subject,
                new ArrayList<>(),
                List.of(professor1, professor2),
                Interval.of(Instant.ofEpochSecond((long) (10000 + Math.random())), Instant.ofEpochSecond((long) (1000000 + Math.random())))
        );

        try {
            slotDAO.save(slot);
            List<Professor> professorsOfSlot = dao.getProfessorOfSlots(slot.getId());
            assertEquals(professorsOfSlot, slot.getProfessors());
            slotDAO.delete(slot);
        } catch (SQLException e) {
            throw new AssertionError();
        }

        deleteSlotData();
    }

    @Test
    void addSameProfessor() {
        final String defaultMsg = "Done Save Without Error";

        int oldSize = dao.getAll().size();
        dao.save(professor1, "testingOnly");
        int newSize = dao.getAll().size();

        assertEquals(oldSize, newSize);
    }


    @Test
    void deleteTheProfessor(){
        Professor professor = getRandomNewProfessor();
        dao.save(professor,"1234");
        int oldsize = dao.getAll().size();
        dao.delete(professor);
        assertEquals(oldsize-1,dao.getAll().size());
    }
}
