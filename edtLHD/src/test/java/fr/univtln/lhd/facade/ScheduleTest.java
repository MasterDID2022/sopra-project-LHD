package fr.univtln.lhd.facade;

import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.dao.slots.SlotDAO;
import fr.univtln.lhd.model.entities.dao.users.StudentDAO;
import fr.univtln.lhd.model.entities.users.Student;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.threeten.extra.Interval;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleTest {

    @Test
    //Kind of a test
    void getSchedule() throws SQLException {
        StudentDAO dao = StudentDAO.getInstance();
        SlotDAO sdoa = SlotDAO.getInstance();
        Interval timerange = sdoa.get(3).get().getTimeRange();
        Student student = dao.get(1).get();
        Assertions.assertEquals(sdoa.get(3).get(),Schedule.getSchedule(student,timerange).get(0));
    }

    void testGetSchedule() {
        //TODO
    }

    void testGetSchedule1() {
        //TODO
    }

    void testGetSchedule2() {
        //TODO
    }
}