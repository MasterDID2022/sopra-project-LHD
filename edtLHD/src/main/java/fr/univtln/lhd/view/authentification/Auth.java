package fr.univtln.lhd.view.authentification;

import fr.univtln.lhd.facade.Schedule;
import fr.univtln.lhd.model.entities.users.User;
import lombok.Getter;

@Getter
public class Auth {

    public enum AuthType { ADMIN, STUDENT, PROFESSOR, GUEST }

    private final AuthType type;

    private User authUser;

    private Auth(AuthType type, String email, String password) {
        this.type = type;

        switch (type) {
            case GUEST -> authUser = null;
            case ADMIN -> authUser = Schedule.getAdminFromAuth(email, password).orElseThrow(RuntimeException::new);
            case PROFESSOR -> authUser = Schedule.getProfessorFromAuth(email, password).orElseThrow(RuntimeException::new);
            case STUDENT -> authUser = Schedule.getStudentFromAuth(email, password).orElseThrow(RuntimeException::new);
        }
    }

    public static Auth authAsStudent(String email, String password) { return new Auth(AuthType.STUDENT, email, password); }
    public static Auth authAsProfessor(String email, String password) { return new Auth(AuthType.PROFESSOR, email, password); }
    public static Auth authAsAdmin(String email, String password) { return new Auth(AuthType.ADMIN, email, password); }
    public static Auth authAsGuest() { return new Auth(AuthType.GUEST, "", ""); }

    public boolean isStudent() { return type == AuthType.STUDENT; }
    public boolean isProfessor() { return type == AuthType.PROFESSOR; }
    public boolean isAdmin() { return type == AuthType.ADMIN; }
    public boolean isGuest() { return type == AuthType.GUEST; }

    public String getGreetingsMessage() {
        return isGuest() ?
                "Connecté en tant qu'Invité."
                : "Bonjour "
                + authUser.getFname().toUpperCase()
                + ", " + authUser.getName()
                + ". (compte " + authUser.getClass().getSimpleName() + ")";
    }
}
