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
    private final String get="SELECT * FROM USERS WHERE ID=?";
    private final String getAll="SELECT * FROM USERS";
    private final String save="INSERT INTO USERS VALUES (DEFAULT,?, ?,?,?)";
    private final String update="UPDATE USERS SET name=?, fname=? ,email=? WHERE ID=?";
    private final String delete="DELETE FROM USERS WHERE ID=?";
    private final String getAllGroup="SELECT * FROM GROUP_USER WHERE ID_USER=?";
    private final String saveGroup="INSERT INTO GROUP_USER VALUES (?, ?)";

    private StudentDAO (){ }

    public static StudentDAO getInstance (){ return new StudentDAO(); }


    /**
     * Compile a string into a prepared statement
     * @param statement
     * @return the string into a PreparedStatement
     */

    /**
     * Getter for one Student
     * @param id numerical long identifier for getting the Student
     * @return May return one Student
     */
    @Override
    public Optional<Student> get(long id) throws SQLException {
        Student result=null;
        try (Connection conn = Datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(get)
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
     * take a student and add every group that is in, by using the database
     * @param student
     */
    private void updateStudentGroup(Student student) throws SQLException {
        try (Connection conn = Datasource.getConnection();
             PreparedStatement getAllGroupDao = conn.prepareStatement(getAllGroup)) {
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
            PreparedStatement stmt = conn.prepareStatement(getAll);
             )
        {
            ResultSet rs = stmt.executeQuery();
            log.error(rs.isClosed()+"");
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
                PreparedStatement stmt = conn.prepareStatement(save)
                )
        {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getFname());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, "NO_PASSWORD");
             int i = stmt.executeUpdate();
             log.info(i+" inserts");
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
    public void save(Student student, String password) throws SQLException {
        try(
                Connection conn = Datasource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(save,RETURN_GENERATED_KEYS);
                ){
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getFname());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, password);
            stmt.executeUpdate();
            ResultSet idSet = stmt.getGeneratedKeys();
            idSet.next();
            student.setId(idSet.getLong(1));

        } catch (SQLException | IdException e){
            log.error(e.getMessage());
        }
    }

    /**
     * save the group of the student with the Id inside group User
     * @param student
     * @throws SQLException
     */
    private void SaveStudentGroup(Student student) throws SQLException {
        if (student.getStudendGroup() != null) {
            GroupDAO dao = GroupDAO.getInstance();
            dao.save((Group) student.getStudendGroup());
            try (Connection conn = Datasource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(saveGroup)
            ) {

                stmt.setLong(1, ((Group) student.getStudendGroup()).getId());
                stmt.setLong(2, student.getId());
                stmt.executeUpdate();
            }
        }
    }

    /**
     * Update the data of Student in the database, without modifying the object student,
     * to get the new student use <code>updateAndGet</code>
     * @param student a Student
     */
    @Override
    public Student update(Student student) throws SQLException {
        try(
                Connection conn = Datasource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(update)
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
            PreparedStatement stmt = conn.prepareStatement(delete);
        ) {
            stmt.setLong(1,student.getId());
            stmt.executeUpdate();
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }
    }

}
