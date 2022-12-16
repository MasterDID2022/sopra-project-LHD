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
    private final PreparedStatement getAll;
    private final PreparedStatement get;
    private final PreparedStatement save;
    private final PreparedStatement update;
    private final PreparedStatement delete;

    public ProfessorDAO() throws SQLException {
        Connection connection = Datasource.getInstance().getConnection();
        this.get = connection.prepareStatement("SELECT * FROM PROFESSORS WHERE ID=?");
        this.getAll = connection.prepareStatement("SELECT * FROM PROFESSORS");
        this.save = connection.prepareStatement("INSERT INTO PROFESSORS VALUES (DEFAULT, ?, ?, ?, ?, ?)",RETURN_GENERATED_KEYS);
        this.update = connection.prepareStatement("UPDATE PROFESSORS SET name=?, fname=? ,email=?,title=? WHERE ID=?");
        this.delete = connection.prepareStatement("DELETE FROM PROFESSORS WHERE ID=?");
    }


    /**
     * Getter for one Lecturer
     * @param id numerical long identifier for getting the Lecturer
     * @return May return one Lecturer
     */
    @Override
    public Optional<Professor> get(long id) {
        Optional<Professor> result = Optional.empty();
        try {
            get.setLong(1, id);
            ResultSet rs = get.executeQuery();
            if (rs.next()) {
                result = Optional.of(
                        Professor.of(
                                rs.getString("NAME"),
                                rs.getString("FNAME"),
                                rs.getString("EMAIL"),
                                rs.getString("TITLE"))
                );
                result.get().setId(rs.getLong("ID"));
            }
        }catch (SQLException | IdException e){
            log.error(e.getMessage());
        }
        return result;
    }

    /**
     * Getter for all Lecturer
     * @return List of all Lecturer
     */
    @Override
    public List<Professor> getAll() {
        List<Professor> professorList = new ArrayList<>();
        try {
            ResultSet rs = getAll.executeQuery();
            while (rs.next()) {
                Professor professor = Professor.of(
                                rs.getString("NAME"),
                                rs.getString("FNAME"),
                                rs.getString("EMAIL"),
                                rs.getString("TITLE"));
                professor.setId(rs.getLong("ID"));
                professorList.add(professor);
            }
        } catch (SQLException | IdException e){
            log.error(e.getMessage());
        }
        return professorList;
    }


    /**
     * Save Lecturer t to Database
     * <!>SHOULD ONLY BE USED FOR TEST</!>
     * this methode save a User without a password
     * please use save(Lecturer s,String password)
     * @param professor Lecturer object to save
     */
    @Override
    public void save(Professor professor) {
        try{
            save.setString(1, professor.getName());
            save.setString(2, professor.getFname());
            save.setString(3, professor.getEmail());
            save.setString(4, "NO_PASSWORD");
            save.setString(5, professor.getTitle());
            save.executeUpdate();
        } catch (SQLException e){
            log.error(e.getMessage());
        }
        log.error("Not supposed to be used");
    }

    /**
     * Save Lecturer t to Database and add the ID generate by the database
     * to lecturer, if the lecturer exist it will only update the ID
     * @param professor Lecturer object to save
     * @param password password to save inside the database
     */
    public void save(Professor professor, String password) {
        ResultSet result;
        try{
            save.setString(1, professor.getName());
            save.setString(2, professor.getFname());
            save.setString(3, professor.getEmail());
            save.setString(4, password);
            save.setString(5, professor.getEmail());
            save.executeUpdate();
            ResultSet id_set = save.getGeneratedKeys();
            id_set.next();
            professor.setId(id_set.getLong(1));
        } catch (SQLException | IdException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Update the data of Lecturer in the database, without modifying the object lecturer,
     * to get the new lecturer use <code>updateAndGet</code>
     * @param professor a Lecturer
     */
    @Override
    public Professor update(Professor professor) throws IdException {
        try {
            update.setString(1,professor.getName());
            update.setString(2, professor.getFname());
            update.setString(3,professor.getEmail());
            update.setString(4,professor.getTitle());
            update.setLong(5,professor.getId());
            update.executeUpdate();
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }
        return professor;
    }


    /**
     * Delete Lecturer from Database
     * @param professor Lecturer to be deleted from the database
     */
    @Override
    public void delete(Professor professor) {
        try {
            delete.setLong(1, professor.getId());
            delete.executeUpdate();
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }
    }

}
