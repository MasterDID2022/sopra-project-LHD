package fr.univtln.lhd.model.entities.dao.users;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.users.Admin;
import fr.univtln.lhd.model.entities.users.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class AdminDAOTest {
    public static final AdminDAO dao = AdminDAO.of();
    private Admin admin;

    private Admin getRandomNewAdmin(){
        return Admin.of("UnitTest","UnitTestFirstName",
                "UnitTestName.Firstname"+Math.random()+"@email.com","ScienceUnique");
    }

    
    private Admin getTheTestAdmin(){
        return Admin.of("TheTestAdmin","UnitTestFirstName",
                "UnitTestName.Firstname@email.com","RandomSciences");
    }

    @BeforeEach
    public void initializeTestEnvironment() {
        admin = this.getRandomNewAdmin();
        dao.save(admin, "1234");
    }

    @AfterEach
    public void deleteTestEnvironement() {
        dao.delete(admin);
    }

    @Test
    void CreateADAO(){
        Assertions.assertNotNull(dao);
    }

    @Test
    void addNewAdmin(){
        Admin admin = getRandomNewAdmin();
        int oldSize = dao.getAll().size();
        dao.save(admin,"1234");
        assertEquals(oldSize+1,dao.getAll().size());
        dao.delete(admin);
    }

    @Test
    void getAdminFromAuthTest(){
        try {
            Admin authGetterAdmin = dao.get(admin.getEmail(), "1234").orElseThrow(SQLException::new);
            assertEquals(admin, authGetterAdmin);
        }catch (SQLException e){
            log.error(e.getMessage());
            throw new AssertionError();
        }
    }


    @Test
    void shouldNotGetAdminFromAuthTest() throws SQLException {
        Admin fetchedAdmin = dao.get(admin.getEmail(), "WrongPassword").orElse(Admin.of("Or","else","ttorelse@mail.fr","fac"));
        Assertions.assertNotEquals(admin,fetchedAdmin);
    }

    @Test
    void updateAnAdmin() throws IdException {
        Admin admin = getRandomNewAdmin();
        Admin admin1 = Admin.of(admin.getName()+"1",admin.getFname()+"1",admin.getEmail()+"1", admin.getFaculty());
        dao.save(admin,"1234");
        admin1.setId(admin.getId());
        dao.update(admin1);
        assertEquals(dao.get(admin.getId()).orElseThrow(AssertionError::new),admin1);
        dao.delete(admin);
    }

    @Test
    void addSameAdmin(){
        Admin admin = getTheTestAdmin();
        dao.save(admin,"1234");
        int oldSize = dao.getAll().size();
        dao.save(admin,"1234");
        assertEquals(oldSize,dao.getAll().size());
        dao.delete(admin);
    }


    @Test
    void addSameAdminWhitoutPassword(){
        Admin admin = getTheTestAdmin();
        dao.save(admin);
        int oldSize = dao.getAll().size();
        dao.save(admin,"1234");
        assertEquals(oldSize,dao.getAll().size());
        dao.delete(admin);
    }


    @Test
    void deleteTheAdmin(){
        Admin admin = getRandomNewAdmin();
        dao.save(admin,"1234");
        int oldSize = dao.getAll().size();
        dao.delete(admin);
        assertEquals(oldSize-1,dao.getAll().size());
    }

}