package fr.univtln.lhd.model.entities.dao.users;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.dao.Datasource;
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
    private final String GET="SELECT * FROM PROFESSORS WHERE ID=?";

    private final String GET_ALL="SELECT * FROM PROFESSORS";
    private final String GET_PROFEESOR_OF_SLOT = "SELECT ID_PROFESSOR from PROFESSOR_SLOT where id_SLOT= ?";
    private final String SAVE="INSERT INTO PROFESSORS VALUES (DEFAULT, ?, ?, ?, ?, ?)";
    private final String UPDATE="UPDATE PROFESSORS SET name=?, fname=? ,email=?,title=? WHERE ID=?";
    private final String DELETE="DELETE FROM PROFESSORS WHERE ID=?";


    private ProfessorDAO(){}

    public static ProfessorDAO of(){ return new ProfessorDAO(); }

    /**
     * Getter for one Professor
     * @param id numerical long identifier for getting the Professor
     * @return May return one Professor
     */
    @Override
    public Optional<Professor> get(long id) {
        Optional<Professor> fetchedProfessor = Optional.empty();
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET)
        ){
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                fetchedProfessor = Optional.of(
                        Professor.of(
                                resultSet.getString("NAME"),
                                resultSet.getString("FNAME"),
                                resultSet.getString("EMAIL"),
                                resultSet.getString("TITLE"))
                );
                fetchedProfessor.get().setId(resultSet.getLong("ID"));
            }
        }catch (SQLException | IdException e){
            log.error(e.getMessage());
        }
        return fetchedProfessor;
    }

    /**
     * Getter for all Professor
     * @return List of all Professor
     */
    @Override
    public List<Professor> getAll() {
        List<Professor> professorList = new ArrayList<>();
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_ALL)
        ){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Professor fetchedProfessor = Professor.of(
                                rs.getString("NAME"),
                                rs.getString("FNAME"),
                                rs.getString("EMAIL"),
                                rs.getString("TITLE"));
                fetchedProfessor.setId(rs.getLong("ID"));
                professorList.add(fetchedProfessor);
            }
        } catch (SQLException | IdException e){
            log.error(e.getMessage());
        }
        return professorList;
    }


    /**
     * Save Professor t to Database
     * <!>SHOULD ONLY BE USED FOR TEST</!>
     * this methode save a User without a password
     * please use save(Professor s,String password)
     * @param professor Professor object to save
     */
    @Override
    public void save(Professor professor) {
        try(Connection conn = Datasource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SAVE)
        ){
            stmt.setString(1, professor.getName());
            stmt.setString(2, professor.getFname());
            stmt.setString(3, professor.getEmail());
            stmt.setString(4, "NO_PASSWORD");
            stmt.setString(5, professor.getTitle());
            stmt.executeUpdate();
        } catch (SQLException e){
            log.error(e.getMessage());
        }
        log.error("Not supposed to be used");
    }

    /**
     * Save Professor t to Database and add the ID generate by the database
     * to professor, if the professor exist it will only update the ID
     * @param professor Professor object to save
     * @param password password to save inside the database
     */
    public void save(Professor professor, String password) {
        ResultSet result;
        try(Connection conn = Datasource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SAVE, RETURN_GENERATED_KEYS)
        ){
            stmt.setString(1, professor.getName());
            stmt.setString(2, professor.getFname());
            stmt.setString(3, professor.getEmail());
            stmt.setString(4, password);
            stmt.setString(5, professor.getEmail());
            stmt.executeUpdate();
            ResultSet id_set = stmt.getGeneratedKeys(); id_set.next();
            professor.setId(id_set.getLong(1));
        } catch (SQLException | IdException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Update the data of Professor in the database, without modifying the object professor,
     * to get the new professor use <code>updateAndGet</code>
     * @param professor a Professor
     */
    @Override
    public Professor update(Professor professor) throws IdException {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)
        ){
            stmt.setString(1,professor.getName());
            stmt.setString(2, professor.getFname());
            stmt.setString(3,professor.getEmail());
            stmt.setString(4,professor.getTitle());
            stmt.setLong(5,professor.getId());
            stmt.executeUpdate();
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }
        return professor;
    }


    /**
     *Take the Id of a Slot and return every professors related from this slot
     * by using the professor_slot table in the database
     * @param slotId an Id of a sot
     * @return List of Professor
     */
    public List<Professor> getProfessorOfSlots(long slotId){
        List<Professor> ProfessorsOfSlot = new ArrayList<>();
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET_PROFEESOR_OF_SLOT)
        ){
            stmt.setLong(1,slotId);
            stmt.executeQuery();
            ResultSet rs =stmt.getResultSet();
            while (rs.next()) {
                ProfessorsOfSlot.add(get(rs.getLong(1)).get());
        }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return ProfessorsOfSlot;
    }


    /**
     * Delete Professor from Database
     * @param professor Professor to be deleted from the database
     */
    @Override
    public void delete(Professor professor) {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE)
        ){
            stmt.setLong(1, professor.getId());
            stmt.executeUpdate();
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }
    }

}
