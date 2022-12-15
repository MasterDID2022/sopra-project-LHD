package fr.univtln.lhd.model.entities.dao.user;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.dao.Datasource;
import fr.univtln.lhd.model.entities.users.Admin;
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
public class AdminDAO implements DAO<Admin> {
    private final PreparedStatement getAll;
    private final PreparedStatement get;
    private final PreparedStatement save;
    private final PreparedStatement update;
    private final PreparedStatement delete;

    public AdminDAO() throws SQLException {
        Connection connection = Datasource.getInstance().getConnection();
        this.get = connection.prepareStatement("SELECT * FROM ADMINS WHERE ID=?");
        this.getAll = connection.prepareStatement("SELECT * FROM ADMINS");
        this.save = connection.prepareStatement("INSERT INTO ADMINS VALUES (DEFAULT, ?, ?, ?, ?, ?)",RETURN_GENERATED_KEYS);
        this.update = connection.prepareStatement("UPDATE ADMINS SET name=?, fname=? ,email=?,title=? WHERE ID=?");
        this.delete = connection.prepareStatement("DELETE FROM ADMINS WHERE ID=?");
    }


    /**
     * Getter for one Lecturer
     * @param id numerical long identifier for getting the Lecturer
     * @return May return one Lecturer
     */
    @Override
    public Optional<Admin> get(long id) {
        Optional<Admin> result = Optional.empty();
        try {
            get.setLong(1, id);
            ResultSet rs = get.executeQuery();
            if (rs.next()) {
                result = Optional.of(
                        Admin.of(
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
    public List<Admin> getAll() {
        List<Admin> adminList = new ArrayList<>();
        try {
            ResultSet rs = getAll.executeQuery();
            while (rs.next()) {
                Admin admin = Admin.of(
                                rs.getString("NAME"),
                                rs.getString("FNAME"),
                                rs.getString("EMAIL"),
                                rs.getString("TITLE"));
                admin.setId(rs.getLong("ID"));
                adminList.add(admin);
            }
        } catch (SQLException | IdException e){
            log.error(e.getMessage());
        }
        return adminList;
    }


    /**
     * Save Lecturer t to Database
     * <!>SHOULD ONLY BE USED FOR TEST</!>
     * this methode save a User without a password
     * please use save(Lecturer s,String password)
     * @param admin Lecturer object to save
     */
    @Override
    public void save(Admin admin) {
        try{
            save.setString(1, admin.getName());
            save.setString(2, admin.getFname());
            save.setString(3, admin.getEmail());
            save.setString(4, "NO_PASSWORD");
            save.setString(5, admin.getFaculty());
            save.executeUpdate();
        } catch (SQLException e){
            log.error(e.getMessage());
        }
        log.error("Not supposed to be used");
    }

    /**
     * Save Lecturer t to Database and add the ID generate by the database
     * to lecturer, if the lecturer exist it will only update the ID
     * @param admin Lecturer object to save
     * @param password password to save inside the database
     */
    public void save(Admin admin, String password) {
        ResultSet result;
        try{
            save.setString(1, admin.getName());
            save.setString(2, admin.getFname());
            save.setString(3, admin.getEmail());
            save.setString(4, password);
            save.setString(5, admin.getEmail());
            save.executeUpdate();
            ResultSet id_set = save.getGeneratedKeys();
            id_set.next();
            admin.setId(id_set.getLong(1));
        } catch (SQLException | IdException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Update the data of Lecturer in the database, without modifying the object lecturer,
     * to get the new lecturer use <code>updateAndGet</code>
     * @param admin a Lecturer
     */
    @Override
    public Admin update(Admin admin) throws IdException {

        try {
            update.setString(1,admin.getName());
            update.setString(2, admin.getFname());
            update.setString(3,admin.getEmail());
            update.setString(4,admin.getFaculty());
            update.setLong(5,admin.getId());
            update.executeUpdate();
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }
        return admin;

    }


    /**
     * Delete Lecturer from Database
     * @param admin Lecturer to be deleted from the database
     */
    @Override
    public void delete(Admin admin) {
        try {
            delete.setLong(1, admin.getId());
            delete.executeUpdate();
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }
    }

}
