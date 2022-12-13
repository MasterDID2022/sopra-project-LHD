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

import static java.sql.Statement.RETURN_GENERATED_KEYS;

@Slf4j
public class LecturerDAO implements DAO<Lecturer> {
    private final PreparedStatement getAll;
    private final PreparedStatement get;
    private final PreparedStatement save;
    private final PreparedStatement update;
    private final PreparedStatement delete;

    public LecturerDAO() throws SQLException {
        Connection connection = initConnection();
        this.get = connection.prepareStatement("SELECT * FROM LECTURERS WHERE ID=?");
        this.getAll = connection.prepareStatement("SELECT * FROM LECTURERS");
        this.save = connection.prepareStatement("INSERT INTO LECTURERS VALUES (DEFAULT, ?, ?, ?, ?, ?)",RETURN_GENERATED_KEYS);
        this.update = connection.prepareStatement("UPDATE LECTURERS SET name=?, fname=? ,email=?,title=? WHERE ID=?");
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
        } catch (SQLException | IdException e){
            log.error(e.getMessage());
        }
        return lecturerList;
    }


    /**
     * Save Lecturer t to Database
     * <!>SHOULD ONLY BE USED FOR TEST</!>
     * this methode save a User without a password
     * please use save(Lecturer s,String password)
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
        log.error("Not supposed to be used");
    }

    /**
     * Save Lecturer t to Database and add the ID generate by the database
     * to lecturer, if the lecturer exist it will only update the ID
     * @param lecturer Lecturer object to save
     * @param password password to save inside the database
     */
    public void save(Lecturer lecturer, String password) {
        ResultSet result;
        try{
            save.setString(1, lecturer.getName());
            save.setString(2, lecturer.getFname());
            save.setString(3, lecturer.getEmail());
            save.setString(4, password);
            save.setString(5, lecturer.getEmail());
            save.executeUpdate();
            ResultSet id_set = save.getGeneratedKeys();
            id_set.next();
            lecturer.setId(id_set.getLong(1));
        } catch (SQLException | IdException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Update the data of Lecturer in the database, without modifying the object lecturer,
     * to get the new lecturer use <code>updateAndGet</code>
     * @param lecturer a Lecturer
     * @param params Map of attributes and values
     */
    @Override
    public void update(Lecturer lecturer, Map params) throws IdException {
        updateAndGet(lecturer,params);
    }

    /**
     * Update Data of Lecturer t and return the new lecturer
     * @param lecturer a Lecturer
     * @param params Map of attributes and values
     */
    public Lecturer updateAndGet(Lecturer lecturer, Map<Object,Object> params) throws IdException {
        String name = lecturer.getName();
        String fname = lecturer.getFname();
        String email = lecturer.getEmail();
        String title = lecturer.getTitle();
        for (Object key : params.keySet()) {
            if (key.equals("name")) {
                name = params.get("name").toString();
            } else if (key.equals("fname")) {
                fname = params.get("fname").toString();
            } else if (key.equals("email")) {
                email = params.get("email").toString();
            } else if (key.equals("title")) {
                title = params.get("title").toString();
            }
        }
        Lecturer updatedLecturer = Lecturer.of(name, fname,email,title);
        updatedLecturer.setId(lecturer.getId());
        try {
            update.setString(1, updatedLecturer.getName());
            update.setString(2, updatedLecturer.getFname());
            update.setString(3,updatedLecturer.getEmail());
            update.setLong(4, updatedLecturer.getId());
            update.executeUpdate();
            return updatedLecturer;
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }
        log.error("did not update the database");
        return lecturer;
    }

    /**
     * Delete Lecturer from Database
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
