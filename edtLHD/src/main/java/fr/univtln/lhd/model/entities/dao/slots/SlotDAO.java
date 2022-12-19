package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.dao.Datasource;
import fr.univtln.lhd.model.entities.slots.Classroom;
import fr.univtln.lhd.model.entities.slots.Group;
import fr.univtln.lhd.model.entities.slots.Slot;
import fr.univtln.lhd.model.entities.slots.Subject;
import lombok.extern.slf4j.Slf4j;
import org.threeten.extra.Interval;

import java.sql.*;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
/*
 */
public class SlotDAO implements DAO<Slot> {
    private SlotDAO () {
    }
    static private final String  get = "select id,date_part('epoch',lower(timerange)) as begin_epoch, date_part('epoch',upper(timerange)) as end_epoch,classroom,memo,subject,type from slots WHERE id = (?)";
    //private final String save = "INSERT INTO slots (DEFAULT,(?),(?),(?),(?),(?)";
    //private final String  update;

    //private final String delete = "DELETE FROM slots WHERE id=(?)";

    private static SlotDAO getInstance () {
        return new SlotDAO();
    }


    /**
     * Getter for one Slot
     * @param id numerical long identifier for getting the Slot
     * @return May return one Slot instance
     */
    @Override
    public  Optional<Slot> get ( long id ){
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(get)
        ){
            stmt.setLong(1,id);
            stmt.executeQuery();
            ResultSet rs =stmt.getResultSet();
            if(!rs.next()) {
                return Optional.empty();
            }
            long fetchedID=rs.getLong(1);
            final Instant fetchedBegin = Instant.ofEpochSecond(rs.getLong(2));
            final Instant fetchedEnd = Instant.ofEpochSecond(rs.getLong(3));
            final Interval fetchedInterval = Interval.of(fetchedBegin,fetchedEnd);
            // final Classroom = ClassroomDAO.get(rs.getLong(4));
            final Classroom fetchedClassroom = Classroom.getInstance("test");
            final String fetchedMemo = rs.getString(5);
            final Subject fetchedSubject = Subject.getInstance("e", 122);
            // final Classroom = ClassroomDAO.get(rs.getLong(6));
            final Slot.SlotType fetchedType = Slot.SlotType.valueOf(rs.getString(7));
            return Optional.of(Slot.getInstance(fetchedID, fetchedType,fetchedClassroom, fetchedSubject, List.of(Group.getInstance("e")),fetchedInterval, fetchedMemo));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Getter for all Slots
     * @return List of all Slots
     */
    @Override
    public List<Slot> getAll() {
        //wip
        return Collections.emptyList();
    }

    /**
     * Save Slot to Database
     * @param slot Slot object to save
     */
    @Override
    public void save(Slot slot) {
        //wip
    }

    /**
     * Update Data of Slot Table
     * @param slot Slot instance to update
     */
    @Override
    public Slot update(Slot slot) throws IdException {
        return null;
    }

    /**
     * Delete Slot instance from Database
     * @param slot Slot object to delete
     */
    @Override
    public void delete(Slot slot) {
        //wip
    }
}
