package fr.univtln.lhd.facade;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.dao.slots.ClassroomDAO;
import fr.univtln.lhd.model.entities.dao.slots.GroupDAO;
import fr.univtln.lhd.model.entities.dao.slots.SlotDAO;
import fr.univtln.lhd.model.entities.dao.slots.SubjectDAO;
import fr.univtln.lhd.model.entities.dao.users.AdminDAO;
import fr.univtln.lhd.model.entities.dao.users.ProfessorDAO;
import fr.univtln.lhd.model.entities.dao.users.StudentDAO;
import fr.univtln.lhd.model.entities.slots.Group;
import fr.univtln.lhd.model.entities.users.Admin;
import fr.univtln.lhd.model.entities.users.Professor;
import fr.univtln.lhd.model.entities.users.Student;
import fr.univtln.lhd.model.entities.slots.Slot;
import lombok.extern.slf4j.Slf4j;
import org.threeten.extra.Interval;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map;

/**
 * facade of the entities/dao and the ihm
 */
@Slf4j
public class Schedule implements Observable{
    private static Map<String, List<Observer>> observerMap = new HashMap<>();

    private static final ZoneId TIME_ZONE = ZoneOffset.systemDefault();
    private static final Schedule schedule = new Schedule();

    private static Schedule instance;

    public static Schedule getInstance(){
        if (instance == null) instance = new Schedule();
        return instance;
    }

    /**
     * Get Professor of Database via email & password
     *
     * @param email    Email of the professor
     * @param password password of the professor
     * @return Professor entity if exist in database, otherwise return null.
     */
    public static Optional<Professor> getProfessorFromAuth(String email, String password) {
        ProfessorDAO dao = ProfessorDAO.of();
        Professor professor = null;
        try {
            professor = dao.get(email, password).orElseThrow(SQLException::new);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.ofNullable(professor);
    }

    /**
     * Get Admin of Database via email & password
     *
     * @param email    Email of the admin
     * @param password password of the admin
     * @return Admin entity if exist in database, otherwise return null.
     */
    public static Optional<Admin> getAdminFromAuth(String email, String password) {
        AdminDAO dao = AdminDAO.of();
        Admin admin = null;
        try {
            admin = dao.get(email, password).orElseThrow(SQLException::new);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.ofNullable(admin);
    }

    /**
     * Take a student and two LocalDate(start/end) and return every slot that the student is
     * in in the timerange of the two LocalDate
     * The student must be persisted or will return an empty array
     *
     * @param student   a persisted Student
     * @param timeStart the start of the timerange
     * @param timeEnd   the end of the timerange
     * @return a List of slot that is in the timerange and with the Groups of the Student
     */
    public static List<Slot> getSchedule(Student student, LocalDate timeStart, LocalDate timeEnd) {
        Interval timerange = schedule.getIntervalOf(timeStart, timeEnd);
        return Schedule.getSchedule(student, timerange);
    }


    /**
     * Take a Group and two LocalDate(start/end) and return every slot that the group is
     * in, in the timerange of the two LocalDate
     * The group must be persisted or will return an empty array
     *
     * @param group     a persisted group
     * @param timeStart the start of the timerange
     * @param timeEnd   the end of the timerange
     * @return a List of slot that is in the timerange with this group
     */
    public static List<Slot> getSchedule(Group group, LocalDate timeStart, LocalDate timeEnd) {
        Interval timerange = schedule.getIntervalOf(timeStart, timeEnd);
        return Schedule.getSchedule(group, timerange);
    }

    /**
     * Take a student and an Interval timerange and return every slot within the timerange
     * where the student is in
     *
     * @param student   a persisted Student
     * @param timerange An Interval
     * @return a List of slot that is in the timerange and with the Groups of the Student
     */
    public static List<Slot> getSchedule(Student student, Interval timerange) {
        List<Slot> slotList = new ArrayList<>();
        if (student.getId() < 0) {
            return slotList;
        }//Might be an edge case
        SlotDAO dao = SlotDAO.getInstance();
        for (Group group :
                student.getStudentGroup()) {
            try {
                List<Slot> allSlotOfGroup = dao.getSlotOfGroup(group);
                slotList.addAll(allSlotOfGroup);
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
        slotList = schedule.filterEverySlotOutsideTimerange(slotList, timerange);
        return slotList;
    }


    /**
     * Take a Group and an Interval and return a List of the slot with this group
     * inside the interval
     * The group must be persisted or will return an empty array
     *
     * @param group     a persisted group
     * @param timerange an Interval
     * @return a List of slot that is in the timerange and with this group
     */
    public static List<Slot> getSchedule(Group group, Interval timerange) {
        if (group.getId() < 0) {
            return new ArrayList<>();
        }//Might be an edge case
        SlotDAO dao = SlotDAO.getInstance();
        List<Slot> allSlotOfGroup = new ArrayList<>();
        try {
            allSlotOfGroup = dao.getSlotOfGroup(group);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return schedule.filterEverySlotOutsideTimerange(allSlotOfGroup, timerange);
    }

    /**
     * Get Student of Database via email & password
     *
     * @param email    Email of the student
     * @param password password of the student
     * @return Student entity if exist in database, otherwise return null.
     */
    public static Optional<Student> getStudentFromAuth(String email, String password) {
        StudentDAO dao = StudentDAO.getInstance();
        Student student = null;
        try {
            student = dao.get(email, password).orElseThrow(SQLException::new);
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return Optional.ofNullable(student);
    }

    /**
     * Take a slot and persisted it inside the database
     * if any necessary attribute is not persisted
     * the methode will fail
     *
     * @return True if failure False else
     */
    public static boolean addToSchedule(Slot slot) {
        SlotDAO dao = SlotDAO.getInstance();
        if (!schedule.verifyIfPersisted(slot).isEmpty()) {
            log.error("This attribute of slot are not persisted",
                    schedule.verifyIfPersisted(slot));
            return true;
        }
        try {
            dao.save(slot);
        } catch (SQLException e) {
            log.error(e.getMessage());
            return true;
        }
        return false;
    }

    /**
     * Take a Slot and delete it in the database
     *
     * @param slot
     * @return false if failure true if it's a success
     */
    public static boolean deleteInSchedule(Slot slot) {
        if (slot.getId() < 0) {
            log.error("slot is not in the database");//Might be better to replace an exception
            return true;
        }
        SlotDAO dao = SlotDAO.getInstance();
        try {
            dao.delete(slot);
        } catch (SQLException e) {
            log.error("no match for slot of ID:" + slot.getId());
            log.error(e.getMessage());
            return true;
        }
        return false;
    }

    /**
     * Take two slot and update the first by the second
     * if the first one is not in the database, the methode
     * will fail
     *
     * @param oldSlot the old slot, persisted
     * @param newSlot the new one, not yet persisted
     * @return false if failure true if it's a success
     */
    public static boolean updateInSchedule(Slot oldSlot, Slot newSlot) {
        SlotDAO slotDAO = SlotDAO.getInstance();
        if (oldSlot.getId() < 0) {
            log.error("slot [" + oldSlot.toString() +
                    "]\nis not in database, furthermore cannot update" +
                    " this slot");
            return true;
        }
        try {
            newSlot.setId(oldSlot.getId());
            slotDAO.update(newSlot);
        } catch (IdException | SQLException e) {
            log.error(e.getMessage());
            return true;
        }
        return false;
    }

    /**
     * Go through every attribute of slot and verify if it is persisted
     * if not it add it to a list and return every
     * state
     *
     * @param slot of potentially not persisted parameter
     */
    private List<String> verifyIfPersisted(Slot slot) {
        List<String> listOfNotpersistedAttribute = new ArrayList<>();
        for (Professor professor :
                slot.getProfessors()) {
            if (professor.getId() < 0) {
                listOfNotpersistedAttribute.add(professor.toString());
            }
        }
        for (Group group :
                slot.getGroup())
            if (group.getId() < 0) {
                listOfNotpersistedAttribute.add(group.toString());
            }
        if (slot.getClassroom().getId() < 0) {
            listOfNotpersistedAttribute.add(slot.getClassroom().toString());
        }
        if (slot.getSubject().getId() < 0) {
            listOfNotpersistedAttribute.add(slot.getSubject().toString());
        }
        return listOfNotpersistedAttribute;
    }

    /**
     * Take a list of Slot and return another List of slot inside the timerange
     * each slot begin or end in the time range which mean that it can overflow
     *
     * @param slotList  list of all the slot
     * @param timerange an Intervalle of the slot to keep
     * @return all the slot of slotList that was inside the timerange
     */
    private List<Slot> filterEverySlotOutsideTimerange(List<Slot> slotList, Interval timerange) {
        List<Slot> slotIntimerange = new ArrayList<>();
        if (slotList.isEmpty()) {
            return slotIntimerange;
        }
        for (Slot slot :
                slotList) {
            if (timerange.overlaps(slot.getTimeRange())) {
                slotIntimerange.add(slot);
            }
        }
        return slotIntimerange;
    }

    /**
     * Take two LocalDate and return an Interval between the two time
     * to be noted LocalDate doesn't have a time therefor each instant
     * start at the beginning of the day 00h00
     * If the two time are on the same day, the whole day is return
     *
     * @param timeStart the starting LocalDate
     * @param timeEnd   the ending LocalDate
     * @return Interval between the
     */
    private Interval getIntervalOf(LocalDate timeStart, LocalDate timeEnd) {
        Instant start = timeStart.atStartOfDay().atZone(TIME_ZONE).toInstant();
        Instant end = timeEnd.atStartOfDay().atZone(TIME_ZONE).toInstant();
        if (start == end) {
            end = timeEnd.atTime(23, 59, 59).atZone(TIME_ZONE).toInstant();
        }
        return Interval.of(start, end);
    }

    @Override
    public void subscribe(String eventName, Observer observer) {
        if (!observerMap.containsKey(eventName))
            observerMap.put(eventName, new ArrayList<>());
        observerMap.get(eventName).add(observer);
    }

    public static void Subscribe(String eventName, Observer observer){
        if (instance == null) getInstance();
        instance.subscribe(eventName, observer);
    }

    @Override
    public void unsubscribe(String eventName, Observer observer) {
        if (!observerMap.containsKey(eventName)) return;
        observerMap.get(eventName).remove(observer);
    }

    public static void Unsubscribe(String eventName, Observer observer){
        if (instance == null) getInstance();
        instance.unsubscribe(eventName, observer);
    }

    @Override
    public void notifyChanges(String eventName, List<EventChange<?>> changes) {
        if (!observerMap.containsKey(eventName)) return;

        for (Observer observer : observerMap.get(eventName))
            observer.udpate(changes);
    }
}
