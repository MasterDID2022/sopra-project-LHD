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
    private final String save="INSERT INTO GROUP_USER VALUES (?, ?)";
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
    private PreparedStatement CompileStatement( String statement) throws SQLException {
        Connection connection = Datasource.getInstance().getConnection();
        return connection.prepareStatement(statement);
    }

    /**
     * Getter for one Student
     * @param id numerical long identifier for getting the Student
     * @return May return one Student
     */
    @Override
    public Optional<Student> get(long id) throws SQLException {
        PreparedStatement getDao; PreparedStatement getAllGroupDao;
        try {
            getDao = CompileStatement(get);
            getAllGroupDao = CompileStatement(getAllGroup);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Optional<Student> result = Optional.empty();
        try {
            getDao.setLong(1, id);
            ResultSet rs = getDao.executeQuery();
            if (rs.next()) {
                result = Optional.of(
                        Student.of(
                                rs.getString("NAME"),
                                rs.getString("FNAME"),
                                rs.getString("EMAIL"))
                );
                result.get().setId(rs.getLong("ID"));
                updateStudentGroup(result.get());
            }
        }catch (SQLException | IdException e){
            log.error(e.getMessage());
        }
        return result;
    }

    /**
     * take a student and add every group that is in, by using the database
     * @param student
     */
    private void updateStudentGroup(Student student) throws SQLException {
        PreparedStatement getAllGroupDao = CompileStatement(getAllGroup);
        getAllGroupDao.setLong(1,student.getId());
        ResultSet resultSetGroupOfStudent = getAllGroupDao.executeQuery();
        GroupDAO dao = GroupDAO.getInstance();
        long  IDgroupStudent;
        while (resultSetGroupOfStudent.next()) {
            IDgroupStudent = resultSetGroupOfStudent.getLong("id_group");
            student.add(dao.get(IDgroupStudent).get());
        }
    }

    /**
     * Getter for all Student
     * @return List of all Student
     */
    @Override
    public List<Student> getAll() throws SQLException {
        List<Student> studentList = new ArrayList<>();
        PreparedStatement getAllDao;
        try {
            getAllDao = CompileStatement(getAll);
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        try {
            ResultSet rs = getAllDao.executeQuery();
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
        PreparedStatement saveDao;
        saveDao = CompileStatement(save);
        try{
            saveDao.setString(1, student.getName());
            saveDao.setString(2, student.getFname());
            saveDao.setString(3, student.getEmail());
            saveDao.setString(4, "NO_PASSWORD");
            saveDao.executeUpdate();
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
        ResultSet result;
        PreparedStatement saveDao;
        saveDao = CompileStatement(save);
        try{
            saveDao.setString(1, student.getName());
            saveDao.setString(2, student.getFname());
            saveDao.setString(3, student.getEmail());
            saveDao.setString(4, password);
            saveDao.executeUpdate();
            ResultSet id_set = saveDao.getGeneratedKeys();
            id_set.next();
            student.setId(id_set.getLong(1));

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
        if (student.getStudendGroup()!=null){
            GroupDAO dao = GroupDAO.getInstance();
            dao.save((Group) student.getStudendGroup());
            PreparedStatement saveGroupDao = CompileStatement(saveGroup);
            saveGroupDao.setLong(1,((Group) student.getStudendGroup()).getId());
            saveGroupDao.setLong(2,student.getId());
            saveGroupDao.executeUpdate();
        }
    }


    /**
     * Update the data of Student in the database, without modifying the object student,
     * to get the new student use <code>updateAndGet</code>
     * @param student a Student
     */
    @Override
    public Student update(Student student) throws SQLException {
        PreparedStatement updateDao;
        updateDao = CompileStatement(update);
        try {
            updateDao.setString(1,student.getName());
            updateDao.setString(2, student.getFname());
            updateDao.setString(3,student.getEmail());
            updateDao.setLong(4,student.getId());
            updateDao.executeUpdate();
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
        PreparedStatement deleteDao;
        deleteDao = CompileStatement(delete);
        try {
            deleteDao.setLong(1,student.getId());
            deleteDao.executeUpdate();
        }
        catch (SQLException e){
            log.error(e.getMessage());
        }
    }

}
