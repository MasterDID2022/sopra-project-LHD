package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.dao.Datasource;
import fr.univtln.lhd.model.entities.dao.users.ProfessorDAO;
import fr.univtln.lhd.model.entities.dao.users.StudentDAO;
import fr.univtln.lhd.model.entities.slots.Group;
import fr.univtln.lhd.model.entities.slots.Slot;
import fr.univtln.lhd.model.entities.users.Student;
import lombok.extern.slf4j.Slf4j;
import org.threeten.extra.Interval;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Slf4j
public class SlotDAO implements DAO<Slot> {
    private SlotDAO () {
    }

    private static final String GET_STMT = "SELECT id, to_char(lower(timerange) , 'YYYY-MM-DD\"T\"HH24:MI:SS.USTZH:TZM')  as begin, to_char(upper(timerange) , 'YYYY-MM-DD\"T\"HH24:MI:SS.USTZH:TZM') as end,classroom,memo,subject,type FROM SLOTS WHERE ID=?";
    private static final String SAVE_STMT = "INSERT INTO SLOTS VALUES (DEFAULT,?::tstzrange,?,?,?,?)";
    private static final String UPDATE_STMT = "UPDATE SLOTS SET TIMERANGE=?::tstzrange, CLASSROOM=?, MEMO=?, SUBJECT=?, TYPE=? WHERE ID=?";

    private static final String GET_SLOT_FROM_GROUP_STMT = "SELECT ID_SLOT FROM GROUP_SLOT WHERE ID_GROUP= ?";

    private static final String GETALL_STMT = "SELECT * FROM SLOTS";

    private static final String DELETE_STMT = "DELETE FROM slots WHERE id=(?)";

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
        Optional<Slot> result = Optional.empty();
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_STMT)
        ) {
            stmt.setLong(1, id);
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            if (!rs.next()) {
                return Optional.empty();
            }
            result = Optional.of(
                    Slot.getInstance(
                            Slot.SlotType.valueOf(rs.getString("TYPE")),
                            ClassroomDAO.getInstance().get(rs.getLong("CLASSROOM")).orElseThrow(SQLException::new),
                            SubjectDAO.getInstance().get(rs.getLong("SUBJECT")).orElseThrow(SQLException::new),
                            new ArrayList<>(GroupDAO.getInstance().getGroupOfSlot(id)), //the returned list is unmodifiable, requiring a copy.
                            ProfessorDAO.of().getProfessorOfSlots(id),

                            Interval.of(Instant.parse(rs.getString("begin")),
                                        Instant.parse(rs.getString("end"))),
                            rs.getString("MEMO")
                    )
            );
            result.get().setId(rs.getLong("ID"));
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        } catch (IdException e) {
            log.error(e.getMessage());
        }
        return result;
    }

    /**
     * Getter for all Slots
     *
     * @return List of all Slots
     */
    @Override
    public List<Slot> getAll () throws SQLException {
        List<Slot> slotList = new ArrayList<>();
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GETALL_STMT);
             ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                slotList.add(get(rs.getLong("ID")).orElseThrow(SQLException::new));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        }
        return slotList;
    }

    private Interval getIntervalOfTimeRange(String timeRange){
        String time = timeRange.substring(2);
        String lower = time.substring(0, time.indexOf(':', 15)+3);
        String upper = time.substring( time.indexOf("\",\"")+3, time.length()-2);
        upper = upper.substring( 0, upper.indexOf(':', 15)+3);
        String[] intervals = new String[] {lower, upper};
        return Interval.of(
                Timestamp.valueOf(intervals[0]).toInstant().atZone(ZoneId.systemDefault()).toInstant(),
                Timestamp.valueOf(intervals[1]).toInstant().atZone(ZoneId.systemDefault()).toInstant()
        );
    }

    /**
     * Take a group and return a List of the slot with this group
     * @param group
     * @return List of slot
     * @throws SQLException
     */
    public  List<Slot> getSlotOfGroup (Group group) throws SQLException {
        List<Slot> slotList= new ArrayList<>();
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_SLOT_FROM_GROUP_STMT)
        ){
            stmt.setLong(1,group.getId());
            stmt.executeQuery();
            ResultSet rs =stmt.getResultSet();
            while (rs.next()) {
                slotList.add(get(rs.getLong(1)).orElseThrow(SQLException::new) );
            }
        }
        catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        }
        return Collections.unmodifiableList(slotList);
    }

    private String getTimeRangeOfInterval(Interval interval){
        return "[\"" + interval.getStart().toString() + "\",\"" + interval.getEnd().toString() + "\")";
    }

    /**
     * Save Slot to Database
     *
     * @param slot Slot object to save
     */
    @Override
    public void save ( Slot slot ) throws SQLException {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SAVE_STMT, RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, getTimeRangeOfInterval(slot.getTimeRange()));
            stmt.setLong(2, slot.getClassroom().getId());
            stmt.setObject(3, slot.getMemo().orElse(null));
            stmt.setLong(4, slot.getSubject().getId());
            stmt.setString(5, slot.getType().name());

            stmt.executeUpdate();
            ResultSet idSet = stmt.getGeneratedKeys();
            idSet.next();
            slot.setId(idSet.getLong(1));
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        } catch (IdException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Update Data of Slot Table
     *
     * @param slot Slot instance to update
     */
    @Override
    public Slot update ( final Slot slot ) throws SQLException {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_STMT)
        ) {
            stmt.setString(1, getTimeRangeOfInterval(slot.getTimeRange()));
            stmt.setLong(2, slot.getClassroom().getId());
            stmt.setObject(3, slot.getMemo().orElse(null));
            stmt.setLong(4, slot.getSubject().getId());
            stmt.setString(5, slot.getType().name());

            stmt.setLong(6, slot.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        }

        return slot;
    }

    /**
     * Delete Slot instance from Database
     *
     * @param slot Slot object to delete
     */
    @Override
    public void delete ( final Slot slot ) throws SQLException {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_STMT)
        ) {
            stmt.setLong(1, slot.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}

