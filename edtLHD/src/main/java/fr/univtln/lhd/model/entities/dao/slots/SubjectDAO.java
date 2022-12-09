package fr.univtln.lhd.model.entities.dao.slots;

import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.slots.Subject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * SubjectDAO implementing DAO interface for Subject Object
 */
public class SubjectDAO implements DAO<Subject> {

    public static String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    public static String USER = "";
    public static String PASS = "";

    private final Connection conn;
    private final PreparedStatement getAll;
    private final PreparedStatement get;
    private final PreparedStatement save;
    //private final PreparedStatement update;
    //private final PreparedStatement delete;

    private SubjectDAO() throws SQLException {
        this.conn = DriverManager.getConnection(DB_URL, USER, PASS);

        this.get = conn.prepareStatement("SELECT * FROM SUBJECT WHERE ID=?");
        this.getAll = conn.prepareStatement("SELECT * FROM SUBJECT");
        this.save = conn.prepareStatement("INSERT INTO SUBJECT VALUES (?,?)");
        //this.update = conn.prepareStatement("UPDATE SUBJECT ")
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
