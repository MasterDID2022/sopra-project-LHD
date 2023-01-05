package fr.univtln.lhd.view.authentification;

import fr.univtln.lhd.facade.Schedule;
import fr.univtln.lhd.model.entities.dao.users.AdminDAO;
import fr.univtln.lhd.model.entities.dao.users.ProfessorDAO;
import fr.univtln.lhd.model.entities.dao.users.StudentDAO;
import fr.univtln.lhd.model.entities.users.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

/**
 * Authentication class for ease of use regarding of authenticating a user into the planning
 */
@Getter
@Slf4j
public class Auth {

    public enum AuthType { ADMIN, STUDENT, PROFESSOR, GUEST }

    private final AuthType type;

    private User authUser;

    /**
     * Base Constructor for Auth class
     * base on its type, gets the Entity User stored in database if exists (should throw an Exception EntityNotFound otherwise)
     * @param type AuthType of Authentication ( ADMIN / STUDENT / PROFESSOR / GUEST )
     * @param email String email of user
     * @param password String password of user
     */
    private Auth(AuthType type, String email, String password) {
        this.type = type;
        switch (type) {
            case GUEST -> authUser = null;
            case ADMIN -> authUser = Schedule.getAdminFromAuth(email, password).orElseThrow(RuntimeException::new);
            case PROFESSOR -> authUser = Schedule.getProfessorFromAuth(email, password).orElseThrow(RuntimeException::new);
            case STUDENT -> authUser = Schedule.getStudentFromAuth(email, password).orElseThrow(RuntimeException::new);
        }
    }

    /**
     * Auth as a Student from its email and password if the Student exists in database
     * @param email Student email
     * @param password Student password
     * @return Auth Entity
     */
    public static Auth authAsStudent(String email, String password) { return new Auth(AuthType.STUDENT, email, password); }

    /**
     * Auth as a Professor from its email and password if the Professor exists in database
     * @param email Professor email
     * @param password Professor password
     * @return Auth Entity
     */
    public static Auth authAsProfessor(String email, String password) { return new Auth(AuthType.PROFESSOR, email, password); }

    /**
     * Auth as a Admin from its email and password if the Admin exists in database
     * @param email Admin email
     * @param password Admin password
     * @return Auth Entity
     */
    public static Auth authAsAdmin(String email, String password) { return new Auth(AuthType.ADMIN, email, password); }

    /**
     * Auth as a Guest
     * no email or password needed
     * @return Auth Entity
     */
    public static Auth authAsGuest() { return new Auth(AuthType.GUEST, "", ""); }

    /**
     * Is the authenticated user type is Student or not
     * @return boolean value (type == AuthType.STUDENT)
     */
    public boolean isStudent() { return type == AuthType.STUDENT; }

    /**
     * Is the authenticated user type is Professor or not
     * @return boolean value (type == AuthType.PROFESSOR)
     */
    public boolean isProfessor() { return type == AuthType.PROFESSOR; }

    /**
     * Is the authenticated user type is Admin or not
     * @return boolean value (type == AuthType.ADMIN)
     */
    public boolean isAdmin() { return type == AuthType.ADMIN; }

    /**
     * Is the authenticated user type is Guest or not
     * @return boolean value (type == AuthType.GUEST)
     */
    public boolean isGuest() { return type == AuthType.GUEST; }

    /**
     * Returns greetings message based on its type
     * Greetings message if the same for each type except Guest
     * @return String greetings message
     */
    public String getGreetingsMessage() {
        return isGuest() ?
                "Connecté en tant qu'Invité."
                : "Bonjour "
                + authUser.getFname().toUpperCase()
                + ", " + authUser.getName()
                + ". (compte " + authUser.getClass().getSimpleName() + ")";
    }
    /**
     * get a combination of email and password and return if it's the
     * database and if so of which User if not it will return Guest
     * @param email
     * @param password
     * @return
     */
    public static Auth.AuthType isAcorectCombination(String email, String password){
        log.info("Try to connect with"+email+"|"+password);
        AdminDAO adminDAO = AdminDAO.of();
        try {
            if (adminDAO.get(email,password).isPresent()){
                return Auth.AuthType.ADMIN;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }


        ProfessorDAO professorDAO = ProfessorDAO.of();
        try {
            if (professorDAO.get(email,password).isPresent()){
                return Auth.AuthType.PROFESSOR;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        StudentDAO studentDAO = StudentDAO.getInstance();
        try {
            if (studentDAO.get(email,password).isPresent()){
                return Auth.AuthType.STUDENT;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }

        return Auth.AuthType.GUEST;
    }

}
