package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.model.entities.slots.Slot;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

@Slf4j
class SlotDAOTest {
    @Test
    void GetSlotTest () {
        try {
            Slot s = SlotDAO.getInstance().get(3).orElseThrow(SQLException::new);
            log.info(s.toString());
            Assertions.assertNotNull(s);
        } catch (SQLException e) {
            throw new AssertionError();
        }
    }

    @Test
    @Disabled("Delete works but insert is a WIP")
    void DeleteSlotTest () {
        try {
            Slot s = SlotDAO.getInstance().get(3).orElseThrow(SQLException::new);
            Assertions.assertNotNull(s);
            SlotDAO.getInstance().delete(s);
        } catch (SQLException e) {
            throw new AssertionError();
        }
    }
}
