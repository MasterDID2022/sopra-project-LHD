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
    private static final String GET="SELECT * FROM USERS WHERE ID=?";
    private static final String GET_ALl="SELECT * FROM USERS";
    private static final String SAVE="INSERT INTO USERS VALUES (DEFAULT,?, ?,?,?)";
    private static final String UPDATE="UPDATE USERS SET name=?, fname=? ,email=? WHERE ID=?";
    private static final String DELETE="DELETE FROM USERS WHERE ID=?";
    private static final String GET_ALL_GROUP="SELECT * FROM GROUP_USER WHERE ID_USER=?";
    private static final String SAVE_GROUP="INSERT INTO GROUP_USER VALUES (?, ?)";

    private static final String GET_STUDENT_AUTH = "SELECT * FROM USERS WHERE EMAIL=? AND PASSWORD=?";

    private StudentDAO (){ }

    public static StudentDAO getInstance (){ return new StudentDAO(); }


    /**
     * Getter for one Student
     * @param id numerical long identifier for getting the Student
     * @return May return one Student
     */
    @Override
    public Optional<Student> get(long id) throws SQLException {
        Student result=null;
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(GET)
        )
        {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                result = Student.of(
                                rs.getString("NAME"),
                                rs.getString("FNAME"),
                                rs.getString("EMAIL"));
                result.setId(rs.getLong("ID"));
                updateStudentGroup(result);
            }
        }catch (SQLException | IdException e){
            log.error(e.getMessage());
        }
        return Optional.ofNullable(result);
    }

    /**
     * Getter for one student, using email and password
     * @param email Unique email of the corresponding student
     * @param password Password of the Student account
     * @return Student Entity, or null if not found
     * @throws SQLException
     */
    public Optional<Student> get(String email, String password) throws SQLException {
        Student result = null;

        try (Connection conn = Datasource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(GET_STUDENT_AUTH)
        ){
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()){
                result = Student.of(
                        rs.getString("NAME"),
                        rs.getString("FNAME"),
                        rs.getString("EMAIL")
                );
                result.setId(rs.getLong("ID"));
                updateStudentGroup(result);
            }
        } catch (SQLException | IdException e){
            log.error(e.getMessage());
        }

        return Optional.ofNullable(result);
    }

    /**
     * take a student and add every group that is in, by using the database
     * @param student
     */
    private void updateStudentGroup(Student student) throws SQLException {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement getAllGroupDao = conn.prepareStatement(GET_ALL_GROUP))
        {
            getAllGroupDao.setLong(1, student.getId());
            ResultSet resultSetGroupOfStudent = getAllGroupDao.executeQuery();
            GroupDAO dao = GroupDAO.getInstance();
            long idGroupStudent;
            while (resultSetGroupOfStudent.next()) {
                idGroupStudent = resultSetGroupOfStudent.getLong("id_group");
                student.add(dao.get(idGroupStudent).orElseThrow());
            }
        }
    }

    /**
     * Getter for all Student
     * @return List of all Student
     */
    @Override
    public List<Student> getAll() throws SQLException {
        List<Student> studentList = new ArrayList<>();
        try (Connection conn = Datasource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(GET_ALl);
             )
        {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Student student = Student.of(
                                rs.getString("NAME"),
                                rs.getString("FNAME"),
                                rs.getString("EMAIL"));
                student.setId(rs.getLong("ID"));
                updateStudentGroup(student);
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
    public void save(Student student) throws SQLException {
        try(
                Connection conn = Datasource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SAVE)
                )
        {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getFname());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, "NO_PASSWORD");
            stmt.executeUpdate();
            saveStudentGroup(student);
        } catch (SQLException e){
            log.error(e.getMessage());
        }
        log.error("Not supposed to be used,Saving without password");
    }

    /**
     * Save Student t to Database and add the ID generate by the database
     * to student, if the student exist it will only update the ID
     * @param student Student object to save
     * @param password password to save inside the database
     */
    public void save(Student student, String password) throws SQLException {
        try(
                Connection conn = Datasource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SAVE,RETURN_GENERATED_KEYS);
                ){
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getFname());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, password);
            stmt.executeUpdate();
            ResultSet idSet = stmt.getGeneratedKeys();
            idSet.next();
            student.setId(idSet.getLong(1));
            saveStudentGroup(student);
        } catch (SQLException | IdException e){
            log.error(e.getMessage());
        }
    }

    /**
     * save the group of the student with the Id inside group User
     * @param student the Student object of which to save each of his groups
     * @throws SQLException
     */
    private void saveStudentGroup(Student student) throws SQLException {
        List<Group> groups = student.getStudentGroup();
        if (groups == null) return;
        GroupDAO groupDAO = GroupDAO.getInstance();
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SAVE_GROUP)
        ) {
            for (Group group : groups){
                if (group.getId()<0) groupDAO.save(group);
                stmt.setLong(1, group.getId());
                stmt.setLong(2, student.getId());
                stmt.executeUpdate();
            }
        }
    }

    /**
     * Update the data of Student in the database, without modifying the object student,
     * @param student a Student
     */
    @Override
    public Student update(Student student) throws SQLException {
        try(
                Connection conn = Datasource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(UPDATE)
                ) {
            stmt.setString(1,student.getName());
            stmt.setString(2, student.getFname());
            stmt.setString(3,student.getEmail());
            stmt.setLong(4,student.getId());
            stmt.executeUpdate();
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
    public void delete(Student student) throws SQLException {
        try(Connection conn = Datasource.getConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE);
        ) {
            stmt.setLong(1,student.getId());
            stmt.executeUpdate();
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }
    }

}
