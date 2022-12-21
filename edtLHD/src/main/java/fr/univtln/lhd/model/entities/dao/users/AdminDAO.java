package fr.univtln.lhd.model.entities.dao.users;

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
    private static final String GET="SELECT * FROM ADMINS WHERE ID=?";

    private static final String GET_ALL="SELECT * FROM ADMINS";
    private static final String SAVE="INSERT INTO ADMINS VALUES (DEFAULT, ?, ?, ?, ?, ?)";
    private static final String UPDATE="UPDATE ADMINS SET name=?, fname=? ,email=?,dpt=? WHERE ID=?";
    private static final String DELETE="DELETE FROM ADMINS WHERE ID=?";

    private AdminDAO(){ };

    public static AdminDAO of (){ return new AdminDAO(); }


    /**
     * Getter for one Admin
     * @param id numerical long identifier for getting the admin
     * @return May return one admin
     */
    @Override
    public Optional<Admin> get(long id) {
        Optional<Admin> result = Optional.empty();
        try  (Connection conn = Datasource.getConnection();
              PreparedStatement stmt = conn.prepareStatement(GET)
        ){
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = Optional.of(
                        Admin.of(
                                rs.getString("NAME"),
                                rs.getString("FNAME"),
                                rs.getString("EMAIL"),
                                rs.getString("DPT"))
                );
                result.get().setId(rs.getLong("ID"));
            }
        }catch (SQLException | IdException e){
            log.error(e.getMessage());
        }
        return result;
    }

    /**
     * Getter for all admin
     * @return List of all admin
     */
    @Override
    public List<Admin> getAll() {
        List<Admin> adminList = new ArrayList<>();
        try  (Connection conn = Datasource.getConnection();
              PreparedStatement stmt = conn.prepareStatement(GET_ALL)
        ){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Admin admin = Admin.of(
                                rs.getString("NAME"),
                                rs.getString("FNAME"),
                                rs.getString("EMAIL"),
                                rs.getString("DPT"));
                admin.setId(rs.getLong("ID"));
                adminList.add(admin);
            }
        } catch (SQLException | IdException e){
            log.error(e.getMessage());
        }
        return adminList;
    }


    /**
     * Save admin t to Database
     * <!>SHOULD ONLY BE USED FOR TEST</!>
     * this methode save a User without a password
     * please use save(admin s,String password)
     * @param admin admin object to save
     */
    @Override
    public void save(Admin admin) {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SAVE)
        ){
            stmt.setString(1, admin.getName());
            stmt.setString(2, admin.getFname());
            stmt.setString(3, admin.getEmail());
            stmt.setString(4, "NO_PASSWORD");
            stmt.setString(5, admin.getFaculty());
            stmt.executeUpdate();
        } catch (SQLException e){
            log.error(e.getMessage());
        }
        log.error("Not supposed to be used");
    }

    /**
     * Save admin to the Database and add the ID generate by the database
     * to admin, if the admin exist it will only update the ID
     * @param admin admin object to save
     * @param password password to save inside the database
     */
    public void save(Admin admin, String password) {
        ResultSet result;
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SAVE, RETURN_GENERATED_KEYS)
        ){
            stmt.setString(1, admin.getName());
            stmt.setString(2, admin.getFname());
            stmt.setString(3, admin.getEmail());
            stmt.setString(4, password);
            stmt.setString(5, admin.getEmail());
            stmt.executeUpdate();
            ResultSet id_set = stmt.getGeneratedKeys();
            id_set.next();
            admin.setId(id_set.getLong(1));
        } catch (SQLException | IdException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Update the data of admin in the database, without modifying the object admin,
     * to get the new admin use <code>updateAndGet</code>
     * @param admin a admin
     */
    @Override
    public Admin update(Admin admin) throws IdException {
        try  (Connection conn = Datasource.getConnection();
              PreparedStatement stmt = conn.prepareStatement(UPDATE)
        ){
            stmt.setString(1,admin.getName());
            stmt.setString(2, admin.getFname());
            stmt.setString(3,admin.getEmail());
            stmt.setString(4,admin.getFaculty());
            stmt.setLong(5,admin.getId());
            stmt.executeUpdate();
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }
        return admin;

    }


    /**
     * Delete admin from Database
     * @param admin admin to be deleted from the database
     */
    @Override
    public void delete(Admin admin) {
        try  (Connection conn = Datasource.getConnection();
              PreparedStatement stmt = conn.prepareStatement(DELETE)
        ){
            stmt.setLong(1, admin.getId());
            stmt.executeUpdate();
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }
    }

}
