package fr.univtln.lhd.model.entities.dao.users;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.users.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class StudentDAOTest {

    public StudentDAO getDAO() {
        StudentDAO st = null;
        try {
            st = new StudentDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return st;
    }

    private Student getRandomNewStudent(){
        Student student = Student.of("UnitTest","UnitTestFirstName",
                "UnitTestName.Firstname"+Math.random()+"@email.com");//A student is a new one if is email is new
        return student;
    }

    
    private Student getTheTestStudent(){
        Student student = Student.of("TheTestStudent","UnitTestFirstName",
                "UnitTestName.Firstname@email.com");
        return student;
    }

    @Test
    void CreateADAO(){
        StudentDAO dao = getDAO();
        Assertions.assertNotNull(dao);
    }

    @Test
    void addNewStudent(){
        StudentDAO dao = getDAO();
        Student student = getRandomNewStudent();
        int oldsize = dao.getAll().size();
        dao.save(student,"1234");
        assertEquals(oldsize+1,dao.getAll().size());
        dao.delete(student);
    }

    @Test
    void updateAstudent() throws IdException {
        StudentDAO dao = getDAO();
        Student student = getRandomNewStudent();
        Student student1 = Student.of(student.getName()+"1",student.getFname()+"1",student.getEmail()+"1");
        dao.save(student,"1234");
        student1.setId(student.getId());
        dao.update(student1);
        assertEquals(dao.get(student.getId()).get(),student1);
    }

    @Test
    void addSameStudent(){
        StudentDAO dao = getDAO();
        Student student = getTheTestStudent();
        dao.save(student,"1234");
        int oldsize = dao.getAll().size();
        dao.save(student,"1234");
        assertEquals(oldsize,dao.getAll().size());
    }


    @Test
    void deleteTheStudent(){
        StudentDAO dao = getDAO();
        Student student = getRandomNewStudent();
        dao.save(student,"1234");
        int oldsize = dao.getAll().size();
        dao.delete(student);
        assertEquals(oldsize-1,dao.getAll().size());
    }

}