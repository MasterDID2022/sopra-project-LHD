package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.slots.Subject;

import java.sql.*;
import java.util.*;

/**
 * SubjectDAO implementing DAO interface for Subject Object
 */
public class SubjectDAO implements DAO<Subject> {

    public static String DB_URL = "jdbc:postgresql://localhost:5432/lhd";
    public static String USER = "postgres";
    public static String PASS = "assAss&n03&N";

    private final Connection conn;
    private final PreparedStatement getAll;
    private final PreparedStatement get;
    private final PreparedStatement save;
    private final PreparedStatement update;
    private final PreparedStatement delete;

    private SubjectDAO() throws SQLException {
        this.conn = initConnection(DB_URL, USER, PASS);

        this.get = conn.prepareStatement("SELECT * FROM SUBJECT WHERE ID=?");
        this.getAll = conn.prepareStatement("SELECT * FROM SUBJECT");
        this.save = conn.prepareStatement("INSERT INTO SUBJECT VALUES (?,?)");
        this.update = conn.prepareStatement("UPDATE SUBJECT SET NAME=? WHERE ID=?");
        this.delete = conn.prepareStatement("DELETE FROM SUBJECT WHERE ID=?");
    }

    /**
     * Getter for one Subject
     * @param id numerical long identifier for getting the Subject
     * @return May return one Subject instance
     */
    @Override
    public Optional<Subject> get(long id) {
        //wip
        return Optional.empty();
    }

    /**
     * Getter for all Subjects
     * @return List of all Subjects
     */
    @Override
    public List<Subject> getAll() {

        List<Subject> subjectList = new ArrayList<>();

        try {
            ResultSet rs = getAll.executeQuery();
            while (rs.next()) {
                Subject s = Subject.getInstance(rs.getString("NAME"), 10);
                subjectList.add(s);
            }
        } catch (SQLException e){
            throw new RuntimeException();
        }

        //wip
        return Collections.emptyList();
    }

    /**
     * Save Subject to Database
     * @param subject Subject object to save
     */
    @Override
    public void save(Subject subject) {
        //wip
    }

    /**
     * Update Data of Subject Table
     * @param subject Subject instance to update
     * @param params Map of attributes and values
     */
    @Override
    public void update(Subject subject, Map<String, String> params) {
        //wip
    }

    /**
     * Delete Subject instance from Database
     * @param subject Subject object to delete
     */
    @Override
    public void delete(Subject subject) {
        //wip
    }
}
