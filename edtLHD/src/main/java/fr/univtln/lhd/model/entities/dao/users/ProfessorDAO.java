package fr.univtln.lhd.model.entities.dao.users;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.dao.Datasource;
import fr.univtln.lhd.model.entities.dao.slots.GroupDAO;
import fr.univtln.lhd.model.entities.users.Professor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Slf4j
public class ProfessorDAO implements DAO<Professor> {
    private static final String ATT_FIRST_NAME="FNAME";
    private static final String ATT_NAME="NAME";
    private static final String ATT_EMAIL="EMAIL";
    private static final String ATT_TITLE="TITLE";
    private static final String GET = "SELECT * FROM PROFESSORS WHERE ID=?";

    private static final String GET_ALL = "SELECT * FROM PROFESSORS";
    private static final String GET_PROFESSOR_OF_SLOT = "SELECT ID_PROFESSOR from PROFESSOR_SLOT where id_SLOT= ?";
    private static final String GET_ALL_PROFESSOR_SLOT_STMT = "SELECT * FROM PROFESSOR_SLOT";
    private static final String SAVE = "INSERT INTO PROFESSORS VALUES (DEFAULT, ?, ?, ?, crypt(?, gen_salt('bf')), ?)";//NOSONAR
    private static final String SAVE_SLOT_STMT = "INSERT INTO PROFESSOR_SLOT VALUES (?, ?)";
    private static final String UPDATE = "UPDATE PROFESSORS SET name=?, fname=? ,email=?,title=? WHERE ID=?";
    private static final String UPDATE_PROFESSOR_SLOT_STMT = "UPDATE PROFESSOR_SLOT SET ID_PROFESSOR=? WHERE ID_SLOT=?";
    private static final String DELETE = "DELETE FROM PROFESSORS WHERE ID=?";
    private static final String DELETE_PROFESSOR_SLOT_STMT = "DELETE FROM PROFESSOR_SLOT WHERE ID_SLOT=? AND ID_PROFESSOR=?";
    private static final String GET_PROFESSOR_AUTH = "SELECT * FROM PROFESSORS WHERE EMAIL=? AND PASSWORD=crypt(?,password)";//NOSONAR


    private ProfessorDAO() {
    }

    public static ProfessorDAO of() {
        return new ProfessorDAO();
    }

    /**
     * Getter for one Professor
     *
     * @param id numerical long identifier for getting the Professor
     * @return May return one Professor
     */
    @Override
    public Optional<Professor> get(long id) {
        Optional<Professor> fetchedProfessor = Optional.empty();
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET)
        ) {
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                fetchedProfessor = Optional.of(
                        Professor.of(
                                resultSet.getString(ATT_NAME),
                                resultSet.getString(ATT_FIRST_NAME),
                                resultSet.getString(ATT_EMAIL),
                                resultSet.getString(ATT_TITLE))
                );
                fetchedProfessor.get().setId(resultSet.getLong("ID"));
            }
        } catch (SQLException | IdException e) {
            log.error(e.getMessage());
        }
        return fetchedProfessor;
    }

    /**
     * Getter for one professor, using email and password
     *
     * @param email    Unique email of the corresponding professor
     * @param password Password of the Professor account
     * @return Professor Entity, or null if not found
     * @throws SQLException
     */
    public Optional<Professor> get(String email, String password) throws SQLException {
        Professor result = null;

        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_PROFESSOR_AUTH)
        ) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                result = Professor.of(
                        rs.getString(ATT_NAME),
                        rs.getString(ATT_FIRST_NAME),
                        rs.getString(ATT_EMAIL),
                        rs.getString(ATT_TITLE)
                );
                result.setId(rs.getLong("ID"));
            }
        } catch (SQLException | IdException e) {
            log.error(e.getMessage());
        }

        return Optional.ofNullable(result);
    }

    /**
     * Getter for all Professor
     *
     * @return List of all Professor
     */
    @Override
    public List<Professor> getAll() {
        List<Professor> professorList = new ArrayList<>();
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL)
        ) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Professor fetchedProfessor = Professor.of(
                        rs.getString(ATT_NAME),
                        rs.getString(ATT_FIRST_NAME),
                        rs.getString(ATT_EMAIL),
                        rs.getString(ATT_TITLE));
                fetchedProfessor.setId(rs.getLong("ID"));
                professorList.add(fetchedProfessor);
            }
        } catch (SQLException | IdException e) {
            log.error(e.getMessage());
        }
        return professorList;
    }

    /**
     * Get all professor associated to a Slot (via table professor_slot)
     *
     * @return List of Professor entity
     * @throws SQLException
     */
    public List<Professor> getAllProfessorSlot() throws SQLException {
        List<Professor> professorList = new ArrayList<>();
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL_PROFESSOR_SLOT_STMT)
        ) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next())
                professorList.add( get( rs.getLong("id_professor") ).orElseThrow(SQLException::new) );
        } catch (SQLException e){
            log.error(e.getMessage());
            throw e;
        }
        return professorList;
    }


    /**
     * Save Professor t to Database
     * <!>SHOULD ONLY BE USED FOR TEST</!>
     * this methode save a User without a password
     * please use save(Professor s,String password)
     *
     * @param professor Professor object to save
     */
    @Override
    public void save(Professor professor) {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SAVE, RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, professor.getName());
            stmt.setString(2, professor.getFname());
            stmt.setString(3, professor.getEmail());
            stmt.setString(4, "NO_PASSWORD");
            stmt.setString(5, professor.getTitle());
            stmt.executeUpdate();
            ResultSet resultSetID = stmt.getGeneratedKeys();
            resultSetID.next();
            professor.setId(resultSetID.getLong(1));
        } catch (SQLException | IdException e) {
            log.error(e.getMessage());
        }
        log.error("Not supposed to be used,Saving without password");
    }

    /**
     * Save Professor t to Database and add the ID generate by the database
     * to professor, if the professor exist it will only update the ID
     *
     * @param professor Professor object to save
     * @param password  password to save inside the database
     */
    public void save(Professor professor, String password) {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SAVE, RETURN_GENERATED_KEYS)
        ) {
            stmt.setString(1, professor.getName());
            stmt.setString(2, professor.getFname());
            stmt.setString(3, professor.getEmail());
            stmt.setString(4, password);
            stmt.setString(5, professor.getTitle());
            stmt.executeUpdate();
            ResultSet resultSetID = stmt.getGeneratedKeys();
            resultSetID.next();
            professor.setId(resultSetID.getLong(1));
        } catch (SQLException | IdException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Save into join table professor_slot, considering both are already saved in their respective tables
     * Should only be used by SlotDAO
     *
     * @param slotId           Slot entity id to save
     * @param professorIdArray Professor entity id to save
     * @throws SQLException thrown Exception in case of Errors, especially if one of the two ids doesn't exist in entity tables
     */
    public void save(long slotId, long[] professorIdArray) throws SQLException {
        Connection conn = Datasource.getConnection();
        try (conn;
             PreparedStatement stmt = conn.prepareStatement(SAVE_SLOT_STMT)
        ) {
            GroupDAO.batchUpdate(slotId, professorIdArray, conn, stmt);
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * Update the data of Professor in the database, without modifying the object professor,
     * to get the new professor use <code>updateAndGet</code>
     *
     * @param professor a Professor
     */
    @Override
    public Professor update(Professor professor) throws IdException {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)
        ) {
            stmt.setString(1, professor.getName());
            stmt.setString(2, professor.getFname());
            stmt.setString(3, professor.getEmail());
            stmt.setString(4, professor.getTitle());
            stmt.setLong(5, professor.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return professor;
    }

    /**
     * Update a professor linked to a slot on the join tables
     * @param slotId the Slot id
     * @param newProfessorId the new Professor id assigned to the Slot
     */
    public void update(long slotId, long newProfessorId) {
        try(Connection conn = Datasource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_PROFESSOR_SLOT_STMT)
        ){
            stmt.setLong(1, newProfessorId);
            stmt.setLong(2, slotId);
            stmt.executeUpdate();
        } catch (SQLException e){
            log.error(e.getMessage());
        }
    }


    /**
     * Take the Id of a Slot and return every professors related from this slot
     * by using the professor_slot table in the database
     *
     * @param slotId an Id of a sot
     * @return List of Professor
     */
    public List<Professor> getProfessorOfSlots(long slotId) {
        List<Professor> professorsOfSlot = new ArrayList<>();
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_PROFESSOR_OF_SLOT)
        ) {
            stmt.setLong(1, slotId);
            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            while (rs.next()) {
                professorsOfSlot.add(get(rs.getLong(1)).orElseThrow(SQLException::new));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return professorsOfSlot;
    }


    /**
     * Delete Professor from Database
     *
     * @param professor Professor to be deleted from the database
     */
    @Override
    public void delete(Professor professor) {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)
        ) {
            stmt.setLong(1, professor.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Delete a professor from a slot in join table professor_slot
     * @param slotId The slot id where the professor need to be removed
     * @param professorId The professor id
     */
    public void delete(long slotId, long professorId){
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_PROFESSOR_SLOT_STMT)
        ) {
            stmt.setLong(1, slotId);
            stmt.setLong(2, professorId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }

}
