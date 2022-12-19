package fr.univtln.lhd.model.entities.dao.users;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.dao.Datasource;
import fr.univtln.lhd.model.entities.dao.slots.GroupDAO;
import fr.univtln.lhd.model.entities.slots.Group;
import fr.univtln.lhd.model.entities.users.Student;
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
public class StudentDAO implements DAO<Student> {
    private final Connection connection;
    private final PreparedStatement getAll;
    private final PreparedStatement get;
    private final PreparedStatement save;
    private final PreparedStatement update;
    private final PreparedStatement delete;
    private final PreparedStatement getAllGroup;
    private final PreparedStatement saveGroup;

    private StudentDAO () throws SQLException {
        this.connection = Datasource.getInstance().getConnection();
        this.get = this.connection.prepareStatement("SELECT * FROM USERS WHERE ID=?");
        this.getAll = this.connection.prepareStatement("SELECT * FROM USERS");
        this.save = this.connection.prepareStatement("INSERT INTO USERS VALUES (DEFAULT, ?, ?, ?, ?)",RETURN_GENERATED_KEYS);
        this.saveGroup = this.connection.prepareStatement("INSERT INTO GROUP_USER VALUES (?, ?)");
        this.getAllGroup = this.connection.prepareStatement("SELECT * FROM GROUP_USER WHERE ID_USER=?");
        this.update = this.connection.prepareStatement("UPDATE USERS SET name=?, fname=? ,email=? WHERE ID=?");
        this.delete = this.connection.prepareStatement("DELETE FROM USERS WHERE ID=?");
    }

    public static StudentDAO getInstance () throws SQLException {
        return new StudentDAO();
    }

    /**
     * Getter for one Student
     * @param id numerical long identifier for getting the Student
     * @return May return one Student
     */
    @Override
    public Optional<Student> get(long id) {
        Optional<Student> result = Optional.empty();
        try {
            get.setLong(1, id);
            ResultSet rs = get.executeQuery();
            if (rs.next()) {
                result = Optional.of(
                        Student.of(
                                rs.getString("NAME"),
                                rs.getString("FNAME"),
                                rs.getString("EMAIL"))
                );
                result.get().setId(rs.getLong("ID"));
                long  IDgroupStudent;
                getAllGroup.setLong(1,result.get().getId());
                ResultSet rsGroup = getAllGroup.executeQuery();
                GroupDAO dao = GroupDAO.getInstance();
                while (rsGroup.next()) {
                    IDgroupStudent = rsGroup.getLong("id_group");
                    result.get().add(dao.get(IDgroupStudent).get());
                }
            }
        }catch (SQLException | IdException e){
            log.error(e.getMessage());
        }
        return result;
    }

    /**
     * Getter for all Student
     * @return List of all Student
     */
    @Override
    public List<Student> getAll() {
        List<Student> studentList = new ArrayList<>();
        long  IDgroupStudent;
        try {
            ResultSet rs = getAll.executeQuery();
            while (rs.next()) {
                Student student = Student.of(
                                rs.getString("NAME"),
                                rs.getString("FNAME"),
                                rs.getString("EMAIL"));
                student.setId(rs.getLong("ID"));
                getAllGroup.setLong(1,student.getId());
                ResultSet rsGroup = getAllGroup.executeQuery();
                GroupDAO dao = GroupDAO.getInstance();
                while (rsGroup.next()) {
                    IDgroupStudent = rsGroup.getLong("id_group");
                    student.add(dao.get(IDgroupStudent).get());
                }
                studentList.add(student);
            }
        } catch (SQLException | IdException e){
            log.error(e.getMessage());
        }
        return studentList;
    }


    /**
     * Save Student t to Database
     * <!>SHOULD ONLY BE USED FOR TEST</!>
     * this methode save a User without a password
     * please use save(Student s,String password)
     * @param student Student object to save
     */
    @Override
    public void save(Student student) {
        try{
            save.setString(1, student.getName());
            save.setString(2, student.getFname());
            save.setString(3, student.getEmail());
            save.setString(4, "NO_PASSWORD");
            save.executeUpdate();
        } catch (SQLException e){
            log.error(e.getMessage());
        }
        log.error("Not supposed to be used");
    }

    /**
     * Save Student t to Database and add the ID generate by the database
     * to student, if the student exist it will only update the ID
     * @param student Student object to save
     * @param password password to save inside the database
     */
    public void save(Student student, String password) {
        ResultSet result;
        try{
            save.setString(1, student.getName());
            save.setString(2, student.getFname());
            save.setString(3, student.getEmail());
            save.setString(4, password);
            save.executeUpdate();
            ResultSet id_set = save.getGeneratedKeys();
            id_set.next();
            student.setId(id_set.getLong(1));
            if (student.getStudendGroup()!=null){
                GroupDAO dao = GroupDAO.getInstance();
                dao.save((Group) student.getStudendGroup());
                saveGroup.setLong(1,((Group) student.getStudendGroup()).getId());
                saveGroup.setLong(2,student.getId());
                save.executeUpdate();
            }
        } catch (SQLException | IdException e){
            log.error(e.getMessage());
        }
    }

    /**
     * Update the data of Student in the database, without modifying the object student,
     * to get the new student use <code>updateAndGet</code>
     * @param student a Student
     */
    @Override
    public Student update(Student student) throws IdException {
        try {
            update.setString(1,student.getName());
            update.setString(2, student.getFname());
            update.setString(3,student.getEmail());
            update.setLong(4,student.getId());
            update.executeUpdate();
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }
        return student;
    }

    /**
     * Delete Student from Database
     * @param student Student to be deleted from the database
     */
    @Override
    public void delete(Student student) {
        try {
            delete.setLong(1,student.getId());
            delete.executeUpdate();
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }
    }

}
