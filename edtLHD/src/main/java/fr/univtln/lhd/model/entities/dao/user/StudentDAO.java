package fr.univtln.lhd.model.entities.dao.user;

import fr.univtln.lhd.exception.IdException;
import fr.univtln.lhd.model.entities.dao.DAO;
import fr.univtln.lhd.model.entities.user.Student;
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
public class StudentDAO implements DAO<Student> {
    private final PreparedStatement getAll;
    private final PreparedStatement get;
    private final PreparedStatement save;
    private final PreparedStatement getIdFromEmail;
    private final PreparedStatement update;
    private final PreparedStatement delete;

    public StudentDAO() throws SQLException {
        Connection connection = initConnection();
        this.get = connection.prepareStatement("SELECT * FROM USERS WHERE ID=?");
        this.getAll = connection.prepareStatement("SELECT * FROM USERS");
        this.save = connection.prepareStatement("INSERT INTO USERS VALUES (DEFAULT, ?, ?, ?, ?)");
        this.update = connection.prepareStatement("UPDATE USERS SET name=?, fname=? ,email=? WHERE ID=?");
        this.delete = connection.prepareStatement("DELETE FROM USERS WHERE ID=?");
        this.getIdFromEmail = connection.prepareStatement("SELECT ID FROM USERS WHERE email=?");
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
            }
        }catch (SQLException e){
            log.error(e.getMessage());
        } catch (IdException e) {
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
        try {
            ResultSet rs = getAll.executeQuery();
            while (rs.next()) {
                Student student = Student.of(
                                rs.getString("NAME"),
                                rs.getString("FNAME"),
                                rs.getString("EMAIL"));
                student.setId(rs.getLong("ID"));
                studentList.add(student);
            }
        } catch (SQLException e){
            log.error(e.getMessage());
        } catch (IdException e) {
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
        log.error("Not suppossed to be used");
    }

    /**
     * Save Student t to Database and add the ID generate by the dabatabase
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

        } catch (SQLException e){
            log.error(e.getMessage());
        }
        try{
            getIdFromEmail.setString(1,student.getEmail());
            result = getIdFromEmail.executeQuery();
            result.next();
            student.setId(result.getLong("ID"));
        } catch (SQLException | IdException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Update the data of Student in the database, without modifying the object student,
     * to get the new student use <code>updateAndGet</code>
     * @param student a Student
     * @param params Map of attributes and values
     */
    @Override
    public void update(Student student, Map params) throws IdException {
        updateAndGet(student,params);
    }

    /**
     * Update Data of Student t and return the new student
     * @param student a Student
     * @param params Map of attributes and values
     */
    public Student updateAndGet(Student student, Map<Object,Object> params) throws IdException {
        String name = student.getName();
        String fname = student.getFname();
        String email = student.getEmail();
        for (Object key : params.keySet())
            if (key.equals("name")) {
                name = params.get("name").toString();
            } else if (key.equals("fname")) {
                fname = params.get("fname").toString();
            } else if (key.equals("email")) {
                email = params.get("email").toString();
            }
        Student updatedStudent = Student.of(name, fname,email);
        updatedStudent.setId(student.getId());
        try {
            update.setString(1, updatedStudent.getName());
            update.setString(2, updatedStudent.getFname());
            update.setString(3,updatedStudent.getEmail());
            update.setLong(4, updatedStudent.getId());
            update.executeUpdate();
            System.out.println(updatedStudent);
            return updatedStudent;
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }
        System.out.println("Did not update the database");
        return student;
    }

    /**
     * Delete Student t from Database
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
