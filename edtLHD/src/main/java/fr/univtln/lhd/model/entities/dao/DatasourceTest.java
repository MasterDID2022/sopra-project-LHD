package fr.univtln.lhd.model.entities.dao;

import fr.univtln.lhd.exception.IdException;
import fr.univtln.lhd.model.entities.dao.user.StudentDAO;
import fr.univtln.lhd.model.entities.user.Student;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
public class DatasourceTest
{
    public static void main(String[] args) {
        try (Connection conn = Datasource.getConnection()) {
            System.out.println("fsjkdfh");
            StudentDAO st = new StudentDAO();
            List<Student> test = st.getAll();
            System.out.println(test);
            Student newStudent =  Student.of("AAAA","TTTT","AAAA@TTT.fr");
            newStudent.setId(6);
            st.delete(newStudent);
            List<Student> test2 = st.getAll();
            System.out.println(test2);
        } catch (SQLException e){
            log.error(e.getMessage());
        } catch (IdException e) {
            throw new RuntimeException(e);
        }
    }
}
