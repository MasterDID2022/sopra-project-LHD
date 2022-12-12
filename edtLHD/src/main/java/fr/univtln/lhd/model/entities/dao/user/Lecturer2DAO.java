package fr.univtln.lhd.model.entities.dao.user;

import fr.univtln.lhd.exception.IdException;
import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.user.Lecturer;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class Lecturer2DAO implements DAO<Lecturer> {
    private final Connection connection;
    private final PreparedStatement getAll;
    private final PreparedStatement get;
    private final PreparedStatement save;
    private final PreparedStatement update;
    private final PreparedStatement delete;

    public Lecturer2DAO() throws SQLException {
        this.connection = initConnection();
        this.get = connection.prepareStatement("SELECT * FROM LECTURERS WHERE ID=?");
        this.getAll = connection.prepareStatement("SELECT * FROM LECTURERS");
        this.save = connection.prepareStatement("INSERT INTO LECTURERS VALUES (DEFAULT, ?, ?, ?, ?, ?)");
        this.update = connection.prepareStatement("UPDATE LECTURERS SET name=?, fname=? ,email=?, title=? WHERE ID=?");
        this.delete = connection.prepareStatement("DELETE FROM LECTURERS WHERE ID=?");
    }

    /**
     * Getter for one Lecturer
     * @param id numerical long identifier for getting the Lecturer
     * @return May return one Lecturer
     */
    @Override
    public Optional<Lecturer> get(long id) {
        Optional<Lecturer> result = Optional.empty();
        try {
            get.setLong(1, id);
            ResultSet rs = get.executeQuery();
            if (rs.next()) {
                result = Optional.of(
                        Lecturer.of(
                                rs.getString("NAME"),
                                rs.getString("FNAME"),
                                rs.getString("EMAIL"),
                                rs.getString("TITLE")));
                result.get().setId(rs.getLong("ID"));
            }
        }catch (SQLException e){
            log.error(e.getMessage());
        } catch (IdException e) {
            log.error(e.getMessage());
        }
        return result;
    }

    /**
     * Getter for all Lecturer
     * @return List of all Lecturer
     */
    @Override
    public List<Lecturer> getAll() {
        List<Lecturer> lecturerList = new ArrayList<>();
        try {
            ResultSet rs = getAll.executeQuery();
            while (rs.next()) {
                Lecturer lecturer = Lecturer.of(
                                rs.getString("NAME"),
                                rs.getString("FNAME"),
                                rs.getString("EMAIL"),
                                rs.getString("TITLE"));
                lecturer.setId(rs.getLong("ID"));
                lecturerList.add(lecturer);
            }
        } catch (SQLException e){
            log.error(e.getMessage());
        } catch (IdException e) {
            log.error(e.getMessage());
        }
        return lecturerList;
    }


    /**
     * Save Lecturer t to Database
     * @param lecturer Lecturer object to save
     */
    @Override
    public void save(Lecturer lecturer) {
        try{
            save.setString(1, lecturer.getName());
            save.setString(2, lecturer.getFname());
            save.setString(3, lecturer.getEmail());
            save.setString(4, "NO_PASSWORD");
            save.setString(5, lecturer.getTitle());
            save.executeUpdate();
        } catch (SQLException e){
            log.error(e.getMessage());
        }
        log.error("Not suppossed to be used");
    }

    /**
     * Save Lecturer t to Database
     * @param lecturer Lecturer object to save
     * @param password password to save inside the database
     */
    public void save(Lecturer lecturer, String password) {
        try{
            save.setString(1, lecturer.getName());
            save.setString(2, lecturer.getFname());
            save.setString(3, lecturer.getEmail());
            save.setString(4, password);
            save.setString(5, lecturer.getTitle());
            save.executeUpdate();
        } catch (SQLException e){
            log.error(e.getMessage());
        }
    }

    /**
     * Update Data of Lecturer t
     * @param lecturer a Lecturer
     * @param params Map of attributes and values
     */
    @Override
    public void update(Lecturer lecturer, Map params) {
        try {
            update.setString(1, lecturer.getName());
            update.setString(2, lecturer.getFname());
            update.setString(3, lecturer.getEmail());
            update.setLong(3, lecturer.getId());
            update.executeUpdate();
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }
    }

    /**
     * Delete lecturer  from Database
     * @param lecturer Lecturer to be deleted from the database
     */
    @Override
    public void delete(Lecturer lecturer) {
        try {
            delete.setLong(1,lecturer.getId());
            delete.executeUpdate();
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }
    }

}
