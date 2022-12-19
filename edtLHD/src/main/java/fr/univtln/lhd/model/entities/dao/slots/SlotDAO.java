package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.dao.Datasource;
import fr.univtln.lhd.model.entities.slots.Classroom;
import fr.univtln.lhd.model.entities.slots.Group;
import fr.univtln.lhd.model.entities.slots.Slot;
import fr.univtln.lhd.model.entities.slots.Subject;
import fr.univtln.lhd.model.entities.users.Professor;
import lombok.extern.slf4j.Slf4j;
import org.threeten.extra.Interval;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
/*
 */
public class SlotDAO implements DAO<Slot> {
    private SlotDAO () {
    }

    private static final String SELECT_STMT = """
            select id,date_part('epoch',lower(timerange)) as begin_epoch,
            date_part('epoch',upper(timerange)) as end_epoch,classroom,memo,subject,type
            from slots WHERE id = (?)
            """;
    //private final String save = "INSERT INTO slots (DEFAULT,(?),(?),(?),(?),(?)";
    //private final String  update;

    private  static final String DELETE_STMT = "DELETE FROM slots WHERE id=(?) CASCADE";

    public static SlotDAO getInstance () {
        return new SlotDAO();
    }


    /**
     * Getter for one Slot
     *
     * @param id numerical long identifier for getting the Slot
     * @return May return one Slot instance
     */
    @Override
    public Optional<Slot> get ( final long id ) throws SQLException {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_STMT)
        ) {
            stmt.setLong(1, id);
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            if (!rs.next()) {
                return Optional.empty();
            }
            long fetchedID = rs.getLong(1);
            final Instant fetchedBegin = Instant.ofEpochSecond(rs.getLong(2));
            final Instant fetchedEnd = Instant.ofEpochSecond(rs.getLong(3));
            final Interval fetchedInterval = Interval.of(fetchedBegin, fetchedEnd);
            final Classroom fetchedClassroom = ClassroomDAO.getInstance().get(rs.getLong(4)).orElseThrow(SQLException::new);
            final String fetchedMemo = rs.getString(5);
            final Subject fetchedSubject = SubjectDAO.getInstance().get(rs.getLong(6)).orElseThrow(SQLException::new);
            final Slot.SlotType fetchedType = Slot.SlotType.valueOf(rs.getString(7));
            final List<Group> fetchedGroup = new ArrayList<>(GroupDAO.getInstance().getGroupOfSlot(id)); //the returned list is unmodifiable, requiring a copy.
            return Optional.of(Slot.getInstance(fetchedID, fetchedType, fetchedClassroom, fetchedSubject, fetchedGroup, List.of(Professor.of("test","test","test@lhd.org","test")),fetchedInterval, fetchedMemo));
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Getter for all Slots
     *
     * @return List of all Slots
     */
    @Override
    public List<Slot> getAll () {
        //wip
        return Collections.emptyList();
    }

    /**
     * Save Slot to Database
     *
     * @param slot Slot object to save
     */
    @Override
    public void save ( Slot slot ) {
        //wip
    }

    /**
     * Update Data of Slot Table
     *
     * @param slot Slot instance to update
     */
    @Override
    public Slot update ( final Slot slot ) {
        return null;
    }

    /**
     * Delete Slot instance from Database
     *
     * @param slot Slot object to delete
     */
    @Override
    public void delete (final  Slot slot ) {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_STMT)){
            final long id = slot.getId();
            if (id<0) { throw new IdException();}
            stmt.setLong(1,id);
            if(stmt.executeUpdate() == 0) {
                throw new IdException("ID" + id + "doesn't exist");
            }
        }

        catch (SQLException | IdException e) {
            log.error(e.getMessage());
        }
        //wip
    }
}

